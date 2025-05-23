package com.finwise.service;

import com.finwise.dto.DashboardDataDTO;
import com.finwise.dto.DashboardDataDTO.*;
import com.finwise.entity.Child;
import com.finwise.entity.EducationPlan;
import com.finwise.repository.ChildRepository;
import com.finwise.repository.EducationPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ChildRepository childRepository;
    private final EducationPlanRepository educationPlanRepository;
    private final EducationPlanService educationPlanService;

    public DashboardDataDTO getDashboardData(Long familyProfileId) {
        DashboardDataDTO dashboardData = new DashboardDataDTO();

        // Get all children for the family
        List<Child> children = childRepository.findByFamilyProfileId(familyProfileId);
        
        // Get all education plans
        List<EducationPlan> educationPlans = children.stream()
            .map(child -> educationPlanRepository.findByChildId(child.getId()))
            .flatMap(List::stream)
            .collect(Collectors.toList());

        // Calculate financial summary
        dashboardData.setFinancialSummary(calculateFinancialSummary(educationPlans));

        // Calculate savings progress
        dashboardData.setSavingsProgress(calculateSavingsProgress(educationPlans));

        // Generate milestones
        dashboardData.setUpcomingMilestones(generateMilestones(educationPlans));

        // Generate alerts
        dashboardData.setFinancialAlerts(generateAlerts(educationPlans));

        // Initialize empty transactions list for now
        dashboardData.setRecentTransactions(new ArrayList<>());

        return dashboardData;
    }

    private FinancialSummaryDTO calculateFinancialSummary(List<EducationPlan> educationPlans) {
        FinancialSummaryDTO summary = new FinancialSummaryDTO();
        
        BigDecimal totalEducationSavings = educationPlans.stream()
            .map(plan -> plan.getCurrentSavings())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalMonthlyContribution = educationPlans.stream()
            .map(plan -> plan.getMonthlyContribution())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        summary.setTotalSavings(totalEducationSavings);
        summary.setEducationFunds(totalEducationSavings);
        summary.setMarriageFunds(BigDecimal.ZERO);
        summary.setEmergencyFunds(BigDecimal.ZERO);
        summary.setInvestmentFunds(BigDecimal.ZERO);
        summary.setMonthlyContribution(totalMonthlyContribution);

        return summary;
    }

    private SavingsProgressDTO calculateSavingsProgress(List<EducationPlan> educationPlans) {
        SavingsProgressDTO progress = new SavingsProgressDTO();

        // Education progress
        ProgressDTO educationProgress = new ProgressDTO();
        BigDecimal totalEducationTarget = educationPlans.stream()
            .map(plan -> plan.getEstimatedTotalCost())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalEducationSavings = educationPlans.stream()
            .map(plan -> plan.getCurrentSavings())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        educationProgress.setTarget(totalEducationTarget);
        educationProgress.setCurrent(totalEducationSavings);

        // Initialize other progress metrics
        ProgressDTO marriageProgress = new ProgressDTO();
        marriageProgress.setTarget(BigDecimal.ZERO);
        marriageProgress.setCurrent(BigDecimal.ZERO);

        ProgressDTO emergencyProgress = new ProgressDTO();
        emergencyProgress.setTarget(new BigDecimal("100000")); // Default target
        emergencyProgress.setCurrent(BigDecimal.ZERO);

        progress.setEducation(educationProgress);
        progress.setMarriage(marriageProgress);
        progress.setEmergency(emergencyProgress);

        return progress;
    }

    private List<MilestoneDTO> generateMilestones(List<EducationPlan> educationPlans) {
        return educationPlans.stream()
            .map(plan -> {
                MilestoneDTO milestone = new MilestoneDTO();
                milestone.setName(plan.getChild().getName() + "'s " + plan.getEducationLevel() + " Education");
                milestone.setType("education");
                milestone.setAmount(plan.getEstimatedTotalCost());
                milestone.setDate(plan.getEstimatedStartYear() + "-01-01");
                return milestone;
            })
            .sorted(Comparator.comparing(MilestoneDTO::getDate))
            .collect(Collectors.toList());
    }

    private List<AlertDTO> generateAlerts(List<EducationPlan> educationPlans) {
        List<AlertDTO> alerts = new ArrayList<>();

        // Check total education savings progress
        BigDecimal totalTarget = educationPlans.stream()
            .map(plan -> plan.getEstimatedTotalCost())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalSavings = educationPlans.stream()
            .map(plan -> plan.getCurrentSavings())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalTarget.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal progress = totalSavings.multiply(new BigDecimal("100")).divide(totalTarget, 2, BigDecimal.ROUND_HALF_UP);
            if (progress.compareTo(new BigDecimal("50")) < 0) {
                AlertDTO alert = new AlertDTO();
                alert.setMessage("Education fund is " + progress.toBigInteger() + "% below target");
                alert.setSeverity("high");
                alerts.add(alert);
            }
        }

        // Add more alerts based on other metrics
        return alerts;
    }
}
