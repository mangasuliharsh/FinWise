package com.finwise.service;

import com.finwise.dto.EducationFundingGapDTO;
import com.finwise.entity.EducationPlan;
import com.finwise.entity.EducationCostReference;
import com.finwise.entity.Child;
import com.finwise.repository.EducationPlanRepository;
import com.finwise.repository.EducationCostReferenceRepository;
import com.finwise.repository.ChildRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.time.Year;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationPlanService {
    private final EducationPlanRepository educationPlanRepository;
    private final EducationCostReferenceRepository costReferenceRepository;
    private final ChildRepository childRepository;

    @Transactional(readOnly = false)
    public EducationPlan createEducationPlan(EducationPlan plan) {
        if (plan == null) {
            throw new IllegalArgumentException("Education plan cannot be null");
        }
        
        if (plan.getChild() == null || plan.getChild().getId() == null) {
            throw new IllegalArgumentException("Child information is required");
        }

        // Get the child entity and set it in the plan
        Child child = childRepository.findById(plan.getChild().getId())
            .orElseThrow(() -> new RuntimeException("Child not found with id: " + plan.getChild().getId()));
            
        plan.setChild(child);
        
        // Validate plan data
        validatePlanData(plan);
        
        calculateProjectedCosts(plan);
        return educationPlanRepository.save(plan);
    }

    public List<EducationPlan> getEducationPlansByChildId(Long childId) {
        return educationPlanRepository.findByChildId(childId);
    }

    public List<EducationPlan> getEducationPlansByFamilyId(Long familyProfileId) {
        // Get all children for the family
        List<Child> children = childRepository.findByFamilyProfileId(familyProfileId);
        
        // Get plans for all children and enrich with child info
        return children.stream()
            .map(child -> {
                List<EducationPlan> plans = educationPlanRepository.findByChildId(child.getId());
                plans.forEach(plan -> {
                    // Set only necessary child fields to avoid circular reference
                    Child childInfo = new Child();
                    childInfo.setId(child.getId());
                    childInfo.setName(child.getName());
                    childInfo.setCurrentEducationLevel(child.getCurrentEducationLevel());
                    plan.setChild(childInfo);
                });
                return plans;
            })
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    public Optional<EducationPlan> getEducationPlanById(Long id) {
        return educationPlanRepository.findById(id);
    }

    @Transactional(readOnly = false)
    public EducationPlan updateEducationPlan(Long id, EducationPlan updatedPlan) {
        return educationPlanRepository.findById(id)
            .map(plan -> {
                plan.setPlanName(updatedPlan.getPlanName());
                plan.setEducationLevel(updatedPlan.getEducationLevel());
                plan.setInstitutionType(updatedPlan.getInstitutionType());
                plan.setEstimatedStartYear(updatedPlan.getEstimatedStartYear());
                plan.setEstimatedEndYear(updatedPlan.getEstimatedEndYear());
                plan.setCurrentSavings(updatedPlan.getCurrentSavings());
                plan.setMonthlyContribution(updatedPlan.getMonthlyContribution());
                plan.setInflationRate(updatedPlan.getInflationRate());
                plan.setNotes(updatedPlan.getNotes());
                calculateProjectedCosts(plan);
                return educationPlanRepository.save(plan);
            })
            .orElseThrow(() -> new RuntimeException("Education plan not found"));
    }

    public void deleteEducationPlan(Long id) {
        educationPlanRepository.deleteById(id);
    }

    private void calculateProjectedCosts(EducationPlan plan) {
        EducationCostReference costReference = costReferenceRepository
            .findTopByEducationLevelAndInstitutionTypeOrderByYearDesc(
                plan.getEducationLevel(), 
                plan.getInstitutionType()
            )
            .orElseThrow(() -> new RuntimeException(
                String.format("No cost reference found for education level '%s' and institution type '%s'. " +
                            "Please ensure the education level and institution type match the available reference data.",
                            plan.getEducationLevel(), plan.getInstitutionType())
            ));

        int yearsDiff = plan.getEstimatedStartYear() - costReference.getYear();
        BigDecimal inflationFactor = BigDecimal.ONE.add(
            plan.getInflationRate().divide(new BigDecimal("100"))
        ).pow(yearsDiff);

        BigDecimal annualCost = costReference.getAverageAnnualCost()
            .multiply(inflationFactor)
            .setScale(2, RoundingMode.HALF_UP);

        int durationYears = plan.getEstimatedEndYear() - plan.getEstimatedStartYear();
        plan.setEstimatedTotalCost(annualCost.multiply(new BigDecimal(durationYears)));
    }

    public EducationFundingGapDTO calculateFundingGap(Long planId) {
        EducationPlan plan = getEducationPlanById(planId)
            .orElseThrow(() -> new RuntimeException("Education plan not found"));

        int monthsUntilStart = (plan.getEstimatedStartYear() - Year.now().getValue()) * 12;
        
        BigDecimal projectedSavings = plan.getCurrentSavings().add(
            plan.getMonthlyContribution().multiply(new BigDecimal(monthsUntilStart))
        );

        BigDecimal fundingGap = plan.getEstimatedTotalCost().subtract(projectedSavings);

        return new EducationFundingGapDTO(
            plan.getEstimatedTotalCost(),
            plan.getCurrentSavings(),
            projectedSavings,
            plan.getMonthlyContribution(),
            fundingGap,
            monthsUntilStart,
            plan.getPlanName(),
            plan.getEducationLevel()
        );
    }

    public List<EducationCostReference> getCostReferences(String educationLevel, String institutionType) {
        if (educationLevel != null && institutionType != null) {
            return costReferenceRepository.findByEducationLevelAndInstitutionType(
                educationLevel, institutionType);
        }
        return costReferenceRepository.findAll();
    }

    private void validatePlanData(EducationPlan plan) {
        if (plan.getInstitutionType() == null || plan.getInstitutionType().trim().isEmpty()) {
            throw new IllegalArgumentException("Institution type is required");
        }
        
        if (plan.getEstimatedStartYear() == 0 || 
            plan.getEstimatedStartYear() < Year.now().getValue()) {
            throw new IllegalArgumentException("Invalid estimated start year");
        }
        
        if (plan.getEstimatedTotalCost() == null) {
            throw new IllegalArgumentException("Estimated total cost cannot be null");
        }
        
        if (plan.getEstimatedTotalCost().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Estimated total cost must be greater than 0");
        }
    }
}
