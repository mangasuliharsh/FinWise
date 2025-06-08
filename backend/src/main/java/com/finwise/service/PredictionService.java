package com.finwise.service;

import com.finwise.entity.FamilyProfile;
import com.finwise.entity.EducationPlan;
import com.finwise.entity.MarriagePlan;
import com.finwise.repository.FamilyProfileRepository;
import com.finwise.repository.EducationPlanRepository;
import com.finwise.repository.MarriagePlanRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PredictionService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FamilyProfileRepository familyProfileRepository;

    @Autowired
    private EducationPlanRepository educationPlanRepository;

    @Autowired
    private MarriagePlanRepository marriagePlanRepository;

    @Value("${prediction.service.url}")
    private String PREDICTION_SERVICE_URL;

    @Value("${allocation.service.url}")
    private String ALLOCATION_SERVICE_URL;

    public void generateAndSaveOptimizedPredictions(Long familyProfileId) {
        try {
            FamilyProfile profile = familyProfileRepository.findById(familyProfileId)
                    .orElseThrow(() -> new RuntimeException("Family profile not found"));

            BigDecimal salary = profile.getMonthlyIncome() != null ? profile.getMonthlyIncome() : BigDecimal.ZERO;
            BigDecimal expenses = profile.getMonthlyExpenses() != null ? profile.getMonthlyExpenses() : BigDecimal.ZERO;
            BigDecimal monthlySavings = salary.subtract(expenses);

            if (monthlySavings.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("No available savings for allocation");
                return;
            }

            // Get all plans
            List<EducationPlan> educationPlans = educationPlanRepository.findByChild_FamilyProfile_Id(familyProfileId);
            List<MarriagePlan> marriagePlans = marriagePlanRepository.findByFamilyProfile_Id(familyProfileId);

            // Use intelligent allocation instead of individual processing
            Map<String, BigDecimal> allocations = performIntelligentAllocation(
                    educationPlans, marriagePlans, monthlySavings);

            // Apply allocations to plans
            applyAllocationsToPlans(educationPlans, marriagePlans, allocations);

        } catch (Exception e) {
            System.err.println("Error in generateAndSaveOptimizedPredictions: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to generate optimized predictions", e);
        }
    }

    private Map<String, BigDecimal> performIntelligentAllocation(
            List<EducationPlan> educationPlans,
            List<MarriagePlan> marriagePlans,
            BigDecimal totalSavings) {

        // Prepare allocation request
        Map<String, Object> allocationRequest = new HashMap<>();
        allocationRequest.put("totalMonthlySavings", totalSavings.doubleValue());
        allocationRequest.put("educationPlans", prepareEducationPlanData(educationPlans));
        allocationRequest.put("marriagePlans", prepareMarriagePlanData(marriagePlans));

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(allocationRequest, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    ALLOCATION_SERVICE_URL + "/allocate",
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return convertAllocationResponse(response.getBody());
            }
        } catch (Exception e) {
            System.err.println("Allocation service failed, falling back to equal distribution: " + e.getMessage());
        }

        // Fallback to equal distribution
        return performEqualAllocation(educationPlans, marriagePlans, totalSavings);
    }

    private List<Map<String, Object>> prepareEducationPlanData(List<EducationPlan> plans) {
        return plans.stream().map(plan -> {
            Map<String, Object> planData = new HashMap<>();
            planData.put("id", plan.getId());
            planData.put("planName", plan.getPlanName());
            // Fix: Use the exact field name expected by Python service
            planData.put("estimated_total_cost", plan.getEstimatedTotalCost().doubleValue());
            planData.put("current_savings", plan.getCurrentSavings().doubleValue());
            planData.put("estimated_start_year", plan.getEstimatedStartYear());
            planData.put("estimated_end_year", plan.getEstimatedEndYear());
            planData.put("inflation_rate", plan.getInflationRate().doubleValue());
            planData.put("monthsLeft", calculateMonthsLeft(plan.getEstimatedStartYear()));
            planData.put("plan_type", "education");
            return planData;
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> prepareMarriagePlanData(List<MarriagePlan> plans) {
        return plans.stream().map(plan -> {
            Map<String, Object> planData = new HashMap<>();
            planData.put("id", plan.getId());
            planData.put("planName", plan.getPlanName());
            // Fix: Use the exact field name expected by Python service
            planData.put("estimated_total_cost", plan.getEstimatedTotalCost().doubleValue());
            planData.put("current_savings", plan.getCurrentSavings().doubleValue());
            planData.put("estimated_year", plan.getEstimatedYear());
            planData.put("inflation_rate", plan.getInflationRate().doubleValue());
            planData.put("monthsLeft", calculateMonthsLeft(plan.getEstimatedYear()));
            planData.put("plan_type", "marriage");
            return planData;
        }).collect(Collectors.toList());
    }


    @SuppressWarnings("unchecked")
    private Map<String, BigDecimal> convertAllocationResponse(Map<String, Object> response) {
        Map<String, BigDecimal> allocations = new HashMap<>();

        if (response.containsKey("allocations")) {
            Map<String, Object> allocationData = (Map<String, Object>) response.get("allocations");

            for (Map.Entry<String, Object> entry : allocationData.entrySet()) {
                String planId = entry.getKey();
                BigDecimal amount = new BigDecimal(entry.getValue().toString());
                allocations.put(planId, amount);
            }
        }

        return allocations;
    }

    private Map<String, BigDecimal> performEqualAllocation(
            List<EducationPlan> educationPlans,
            List<MarriagePlan> marriagePlans,
            BigDecimal totalSavings) {

        Map<String, BigDecimal> allocations = new HashMap<>();
        int totalPlans = educationPlans.size() + marriagePlans.size();

        if (totalPlans == 0) {
            return allocations;
        }

        BigDecimal equalAmount = totalSavings.divide(
                new BigDecimal(totalPlans),
                2,
                RoundingMode.HALF_UP
        );

        // Allocate to education plans
        for (EducationPlan plan : educationPlans) {
            allocations.put("education_" + plan.getId(), equalAmount);
        }

        // Allocate to marriage plans
        for (MarriagePlan plan : marriagePlans) {
            allocations.put("marriage_" + plan.getId(), equalAmount);
        }

        return allocations;
    }

    private void applyAllocationsToPlans(
            List<EducationPlan> educationPlans,
            List<MarriagePlan> marriagePlans,
            Map<String, BigDecimal> allocations) {

        // Apply to education plans
        for (EducationPlan plan : educationPlans) {
            String key = "education_" + plan.getId();
            if (allocations.containsKey(key)) {
                plan.setMonthlyContribution(allocations.get(key));
                educationPlanRepository.save(plan);
                System.out.println("Updated education plan " + plan.getId() +
                        " with allocation: " + allocations.get(key));
            }
        }

        // Apply to marriage plans
        for (MarriagePlan plan : marriagePlans) {
            String key = "marriage_" + plan.getId();
            if (allocations.containsKey(key)) {
                plan.setMonthlyContribution(allocations.get(key));
                marriagePlanRepository.save(plan);
                System.out.println("Updated marriage plan " + plan.getId() +
                        " with allocation: " + allocations.get(key));
            }
        }
    }

    private int calculateMonthsLeft(Integer targetYear) {
        if (targetYear == null) {
            return 60; // Default to 5 years
        }

        int currentYear = Year.now().getValue();
        int monthsLeft = (targetYear - currentYear) * 12;

        if (monthsLeft <= 0) {
            return 12; // Default to 1 year if target year is in the past
        }

        return monthsLeft;
    }
}
