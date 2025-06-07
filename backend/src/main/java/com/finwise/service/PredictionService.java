package com.finwise.service;

import com.finwise.entity.EducationPlan;
import com.finwise.entity.FamilyProfile;
import com.finwise.entity.MarriagePlan;
import com.finwise.repository.EducationPlanRepository;
import com.finwise.repository.FamilyProfileRepository;
import com.finwise.repository.MarriagePlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PredictionService {

    private final FamilyProfileRepository familyProfileRepository;
    private final EducationPlanRepository educationPlanRepository;
    private final MarriagePlanRepository marriagePlanRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String PREDICTION_SERVICE_URL = "http://localhost:5000/predict";

    public void generateAndSavePredictions(Long familyProfileId) {
        try {
            FamilyProfile profile = familyProfileRepository.findById(familyProfileId)
                    .orElseThrow(() -> new RuntimeException("Family profile not found"));

            BigDecimal salary = profile.getMonthlyIncome() != null ? profile.getMonthlyIncome() : BigDecimal.ZERO;
            BigDecimal expenses = profile.getMonthlyExpenses() != null ? profile.getMonthlyExpenses() : BigDecimal.ZERO;

            System.out.println("Processing predictions for family profile ID: " + familyProfileId);
            System.out.println("Salary: " + salary + ", Expenses: " + expenses);

            // Get all education and marriage plans
            List<EducationPlan> educationPlans = educationPlanRepository.findByChild_FamilyProfile_Id(familyProfileId);
            List<MarriagePlan> marriagePlans = marriagePlanRepository.findByFamilyProfile_Id(familyProfileId);

            System.out.println("Found " + educationPlans.size() + " education plans and " + marriagePlans.size() + " marriage plans");

            // Process education plans - iterate through each one
            if (!educationPlans.isEmpty()) {
                processEducationPlans(educationPlans, salary, expenses);
            } else {
                System.out.println("No education plans found for family profile ID: " + familyProfileId);
            }

            // Process marriage plans - iterate through each one
            if (!marriagePlans.isEmpty()) {
                processMarriagePlans(marriagePlans, salary, expenses);
            } else {
                System.out.println("No marriage plans found for family profile ID: " + familyProfileId);
            }

        } catch (Exception e) {
            System.err.println("Error in generateAndSavePredictions: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to generate predictions", e);
        }
    }

    private void processEducationPlans(List<EducationPlan> educationPlans, BigDecimal salary, BigDecimal expenses) {
        System.out.println("Processing " + educationPlans.size() + " education plans");

        // Iterate through each education plan
        for (int i = 0; i < educationPlans.size(); i++) {
            EducationPlan educationPlan = educationPlans.get(i);
            try {
                System.out.println("Processing education plan " + (i + 1) + "/" + educationPlans.size() +
                        " - ID: " + educationPlan.getId());

                // Validate required fields
                if (educationPlan.getEstimatedTotalCost() == null) {
                    System.err.println("Education plan " + educationPlan.getId() + " has null estimated total cost, skipping");
                    continue;
                }

                // Calculate months left until education starts
                int monthsLeft = calculateMonthsLeft(educationPlan.getEstimatedStartYear());

                // Create request matching Python Flask API expectations EXACTLY
                Map<String, Object> request = new HashMap<>();
                request.put("modelType", "education");
                request.put("salary", salary.doubleValue());
                request.put("expenses", expenses.doubleValue());
                request.put("estimatedTotalCost", educationPlan.getEstimatedTotalCost().doubleValue());
                request.put("monthsLeft", monthsLeft);

                System.out.println("Education prediction request " + (i + 1) + ": " + request);

                // Make prediction request
                Map<String, Object> response = makePredictionRequest(request);

                if (response != null && response.containsKey("monthlyContribution")) {
                    BigDecimal monthlyContribution = new BigDecimal(response.get("monthlyContribution").toString());

                    // Update and save the education plan
                    educationPlan.setMonthlyContribution(monthlyContribution);
                    EducationPlan savedPlan = educationPlanRepository.save(educationPlan);

                    System.out.println("Successfully saved education plan " + savedPlan.getId() +
                            " with monthly contribution: " + monthlyContribution);
                } else {
                    System.err.println("Invalid response for education plan " + educationPlan.getId());
                }

            } catch (Exception e) {
                System.err.println("Error processing education plan " + educationPlan.getId() + ": " + e.getMessage());
                e.printStackTrace();
                // Continue with next plan instead of failing completely
            }
        }
    }

    private void processMarriagePlans(List<MarriagePlan> marriagePlans, BigDecimal salary, BigDecimal expenses) {
        System.out.println("Processing " + marriagePlans.size() + " marriage plans");

        // Iterate through each marriage plan
        for (int i = 0; i < marriagePlans.size(); i++) {
            MarriagePlan marriagePlan = marriagePlans.get(i);
            try {
                System.out.println("Processing marriage plan " + (i + 1) + "/" + marriagePlans.size() +
                        " - ID: " + marriagePlan.getId());

                // Validate required fields
                if (marriagePlan.getEstimatedTotalCost() == null) {
                    System.err.println("Marriage plan " + marriagePlan.getId() + " has null estimated total cost, skipping");
                    continue;
                }

                // Calculate months left until marriage
                int monthsLeft = calculateMonthsLeft(marriagePlan.getEstimatedYear());

                // Create request matching Python Flask API expectations EXACTLY
                Map<String, Object> request = new HashMap<>();
                request.put("modelType", "marriage");
                request.put("salary", salary.doubleValue());
                request.put("expenses", expenses.doubleValue());
                request.put("estimatedTotalCost", marriagePlan.getEstimatedTotalCost().doubleValue());
                request.put("monthsLeft", monthsLeft);

                System.out.println("Marriage prediction request " + (i + 1) + ": " + request);

                // Make prediction request
                Map<String, Object> response = makePredictionRequest(request);

                if (response != null && response.containsKey("monthlyContribution")) {
                    BigDecimal monthlyContribution = new BigDecimal(response.get("monthlyContribution").toString());

                    // Update and save the marriage plan
                    marriagePlan.setMonthlyContribution(monthlyContribution);
                    MarriagePlan savedPlan = marriagePlanRepository.save(marriagePlan);

                    System.out.println("Successfully saved marriage plan " + savedPlan.getId() +
                            " with monthly contribution: " + monthlyContribution);
                } else {
                    System.err.println("Invalid response for marriage plan " + marriagePlan.getId());
                }

            } catch (Exception e) {
                System.err.println("Error processing marriage plan " + marriagePlan.getId() + ": " + e.getMessage());
                e.printStackTrace();
                // Continue with next plan instead of failing completely
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

    private Map<String, Object> makePredictionRequest(Map<String, Object> requestData) {
        try {
            // Validate request data before sending
            validateRequestData(requestData);

            // Set proper headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestData, headers);

            System.out.println("Making request to: " + PREDICTION_SERVICE_URL);
            System.out.println("Request payload: " + requestData);

            // Make the request
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    PREDICTION_SERVICE_URL,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                System.out.println("Prediction response: " + response.getBody());
                return response.getBody();
            } else {
                System.err.println("Unexpected response status: " + response.getStatusCode());
                return null;
            }

        } catch (HttpClientErrorException e) {
            System.err.println("HTTP Error: " + e.getStatusCode());
            System.err.println("Response Body: " + e.getResponseBodyAsString());
            System.err.println("Request that failed: " + requestData);
            throw new RuntimeException("Prediction service failed: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error making prediction request: " + e.getMessage());
            System.err.println("Request that failed: " + requestData);
            throw new RuntimeException("Failed to make prediction request", e);
        }
    }

    private void validateRequestData(Map<String, Object> requestData) {
        String[] requiredFields = {"modelType", "salary", "expenses", "estimatedTotalCost", "monthsLeft"};

        for (String field : requiredFields) {
            if (!requestData.containsKey(field) || requestData.get(field) == null) {
                throw new IllegalArgumentException("Missing required field: " + field);
            }
        }

        // Validate modelType
        String modelType = (String) requestData.get("modelType");
        if (!"education".equals(modelType) && !"marriage".equals(modelType)) {
            throw new IllegalArgumentException("Invalid modelType: " + modelType + ". Must be 'education' or 'marriage'");
        }

        // Validate numeric fields
        try {
            double salary = ((Number) requestData.get("salary")).doubleValue();
            double expenses = ((Number) requestData.get("expenses")).doubleValue();
            double cost = ((Number) requestData.get("estimatedTotalCost")).doubleValue();
            int months = ((Number) requestData.get("monthsLeft")).intValue();

            if (salary < 0 || expenses < 0 || cost <= 0 || months <= 0) {
                throw new IllegalArgumentException("Invalid numeric values in request data");
            }
        } catch (ClassCastException | NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric format in request data");
        }
    }
}
