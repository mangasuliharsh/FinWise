package com.finwise.service;

import com.finwise.dto.*;
import com.finwise.entity.*;
import com.finwise.repository.*;
import com.finwise.util.DashboardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DashboardService {

    private final EducationPlanService educationPlanService;
    private final MarriagePlanService marriagePlanService;
    private final InvestmentPlanService investmentPlanService;
    private final PlanTransactionService planTransactionService;
    private final FamilyProfileRepository familyProfileRepository;
    private final DashboardMapper dashboardMapper;

    public DashboardSummaryDTO getDashboardSummary(Long familyProfileId) {
        log.info("Generating dashboard summary for family profile ID: {}", familyProfileId);

        try {
            // Get family profile data
            FamilyProfile familyProfile = familyProfileRepository.findById(familyProfileId)
                    .orElseThrow(() -> new RuntimeException("Family profile not found"));

            // Get education plan summary
            DashboardSummaryDTO.EducationPlanSummary educationSummary = buildEducationPlanSummary(familyProfileId);

            // Get marriage plan summary
            DashboardSummaryDTO.MarriagePlanSummary marriageSummary = buildMarriagePlanSummary(familyProfileId);

            // Get investment portfolio summary
            DashboardSummaryDTO.InvestmentPortfolioSummary investmentSummary = buildInvestmentPortfolioSummary(familyProfileId);

            // Get recent transactions
            List<DashboardSummaryDTO.RecentTransactionSummary> recentTransactions = buildRecentTransactionsSummary(familyProfileId);

            // Calculate totals and growth percentages
            BigDecimal totalSavings = calculateTotalSavings(familyProfile, educationSummary, marriageSummary);
            BigDecimal totalInvestments = investmentSummary.getTotalPortfolioValue();

            return DashboardSummaryDTO.builder()
                    .totalSavings(totalSavings)
                    .monthlyIncome(familyProfile.getMonthlyIncome())
                    .monthlyExpenses(familyProfile.getMonthlyExpenses())
                    .totalInvestments(totalInvestments)
                    .educationPlanSummary(educationSummary)
                    .marriagePlanSummary(marriageSummary)
                    .investmentPortfolioSummary(investmentSummary)
                    .recentTransactions(recentTransactions)
                    .savingsGrowthPercentage(calculateSavingsGrowth(familyProfileId))
                    .investmentGrowthPercentage(calculateInvestmentGrowth(familyProfileId))
                    .totalGoalsProgress(calculateTotalGoalsProgress(educationSummary, marriageSummary))
                    .build();

        } catch (Exception e) {
            log.error("Error generating dashboard summary for family profile ID: {}", familyProfileId, e);
            throw new RuntimeException("Failed to generate dashboard summary", e);
        }
    }

    @Transactional
    public DashboardSummaryDTO refreshDashboardData(Long familyProfileId) {
        log.info("Refreshing dashboard data for family profile ID: {}", familyProfileId);
        return getDashboardSummary(familyProfileId);
    }

    private DashboardSummaryDTO.EducationPlanSummary buildEducationPlanSummary(Long familyProfileId) {
        try {
            List<EducationPlanDTO> educationPlans = educationPlanService.getEducationPlansByFamily(familyProfileId);

            BigDecimal totalCost = educationPlans.stream()
                    .map(EducationPlanDTO::getEstimatedTotalCost)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalSavings = educationPlans.stream()
                    .map(EducationPlanDTO::getCurrentSavings)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal monthlyContribution = educationPlans.stream()
                    .map(EducationPlanDTO::getMonthlyContribution)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Count all plans as active since there's no status field
            int activePlans = educationPlans.size();

            Double progress = totalCost.compareTo(BigDecimal.ZERO) > 0 ?
                    totalSavings.divide(totalCost, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .doubleValue() : 0.0;

            return DashboardSummaryDTO.EducationPlanSummary.builder()
                    .totalPlans(educationPlans.size())
                    .totalEducationCost(totalCost)
                    .totalEducationSavings(totalSavings)
                    .monthlyEducationContribution(monthlyContribution)
                    .educationGoalsProgress(progress)
                    .activePlans(activePlans)
                    .nextMilestone(getNextEducationMilestone(educationPlans))
                    .build();

        } catch (Exception e) {
            log.warn("Error building education plan summary for family ID: {}", familyProfileId, e);
            return DashboardSummaryDTO.EducationPlanSummary.builder()
                    .totalPlans(0)
                    .totalEducationCost(BigDecimal.ZERO)
                    .totalEducationSavings(BigDecimal.ZERO)
                    .monthlyEducationContribution(BigDecimal.ZERO)
                    .educationGoalsProgress(0.0)
                    .activePlans(0)
                    .nextMilestone("No education plans")
                    .build();
        }
    }

    private DashboardSummaryDTO.MarriagePlanSummary buildMarriagePlanSummary(Long familyProfileId) {
        try {
            List<MarriagePlanDTO> marriagePlans = marriagePlanService.getAllPlansByFamily(familyProfileId);

            BigDecimal totalCost = marriagePlans.stream()
                    .map(MarriagePlanDTO::getEstimatedTotalCost)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalSavings = marriagePlans.stream()
                    .map(MarriagePlanDTO::getCurrentSavings)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal monthlyContribution = marriagePlans.stream()
                    .map(MarriagePlanDTO::getMonthlyContribution)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Count all plans as active since there's no status field
            int activePlans = marriagePlans.size();

            Double progress = totalCost.compareTo(BigDecimal.ZERO) > 0 ?
                    totalSavings.divide(totalCost, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .doubleValue() : 0.0;

            return DashboardSummaryDTO.MarriagePlanSummary.builder()
                    .totalPlans(marriagePlans.size())
                    .totalMarriageCost(totalCost)
                    .totalMarriageSavings(totalSavings)
                    .monthlyMarriageContribution(monthlyContribution)
                    .marriageGoalsProgress(progress)
                    .activePlans(activePlans)
                    .nextMilestone(getNextMarriageMilestone(marriagePlans))
                    .build();

        } catch (Exception e) {
            log.warn("Error building marriage plan summary for family ID: {}", familyProfileId, e);
            return DashboardSummaryDTO.MarriagePlanSummary.builder()
                    .totalPlans(0)
                    .totalMarriageCost(BigDecimal.ZERO)
                    .totalMarriageSavings(BigDecimal.ZERO)
                    .monthlyMarriageContribution(BigDecimal.ZERO)
                    .marriageGoalsProgress(0.0)
                    .activePlans(0)
                    .nextMilestone("No marriage plans")
                    .build();
        }
    }

    private DashboardSummaryDTO.InvestmentPortfolioSummary buildInvestmentPortfolioSummary(Long familyProfileId) {
        try {
            List<InvestmentPlanDTO> investmentPlans = investmentPlanService.getAllPlansByFamily(familyProfileId);

            BigDecimal totalValue = investmentPlans.stream()
                    .map(InvestmentPlanDTO::getCurrentSavings)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalGains = investmentPlans.stream()
                    .map(plan -> plan.getCurrentSavings().subtract(plan.getMonthlyContribution()))
                    .filter(gain -> gain.compareTo(BigDecimal.ZERO) > 0)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal monthlyContribution = investmentPlans.stream()
                    .map(InvestmentPlanDTO::getMonthlyContribution)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            InvestmentPlanDTO bestPerforming = investmentPlans.stream()
                    .max(Comparator.comparing(InvestmentPlanDTO::getExpectedReturn))
                    .orElse(null);

            Double portfolioGrowth = calculatePortfolioGrowth(investmentPlans);

            return DashboardSummaryDTO.InvestmentPortfolioSummary.builder()
                    .totalPlans(investmentPlans.size())
                    .totalPortfolioValue(totalValue)
                    .totalGains(totalGains)
                    .monthlyInvestmentContribution(monthlyContribution)
                    .portfolioGrowthPercentage(portfolioGrowth)
                    .bestPerformingPlan(bestPerforming != null ? bestPerforming.getPlanName() : "None")
                    .bestPerformingGrowth(bestPerforming != null ?bestPerforming.getExpectedReturn().doubleValue() : 0.0)
                    .build();

        } catch (Exception e) {
            log.warn("Error building investment portfolio summary for family ID: {}", familyProfileId, e);
            return DashboardSummaryDTO.InvestmentPortfolioSummary.builder()
                    .totalPlans(0)
                    .totalPortfolioValue(BigDecimal.ZERO)
                    .totalGains(BigDecimal.ZERO)
                    .monthlyInvestmentContribution(BigDecimal.ZERO)
                    .portfolioGrowthPercentage(0.0)
                    .bestPerformingPlan("None")
                    .bestPerformingGrowth(0.0)
                    .build();
        }
    }

    private List<DashboardSummaryDTO.RecentTransactionSummary> buildRecentTransactionsSummary(Long familyProfileId) {
        try {
            Optional<FamilyProfile> familyProfile = familyProfileRepository.findById(familyProfileId);
            List<PlanTransaction> recentTransactions = planTransactionService.getRecentTransactions(familyProfile.get());

            return recentTransactions.stream()
                    .limit(10)
                    .map(this::mapToTransactionSummary)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.warn("Error building recent transactions summary for family ID: {}", familyProfileId, e);
            return List.of();
        }
    }

    private DashboardSummaryDTO.RecentTransactionSummary mapToTransactionSummary(PlanTransaction transaction) {
        return DashboardSummaryDTO.RecentTransactionSummary.builder()
                .id(transaction.getId())
                .planType(transaction.getPlanType())
                .action(transaction.getAction())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .date(transaction.getDateTime().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")))
                .planName(getPlanName(transaction))
                .build();
    }

    private String getPlanName(PlanTransaction transaction) {
        try {
            switch (transaction.getPlanType().toUpperCase()) {
                case "EDUCATION":
                    return educationPlanService.getEducationPlan(transaction.getPlanId()).getChildName() + " Education";
                case "MARRIAGE":
                    return marriagePlanService.getMarriagePlan(transaction.getPlanId()).getForName() + " Marriage";
                case "INVESTMENT":
                    return investmentPlanService.getPlan(transaction.getPlanId()).getPlanName();
                default:
                    return "Unknown Plan";
            }
        } catch (Exception e) {
            return "Plan #" + transaction.getPlanId();
        }
    }

    private BigDecimal calculateTotalSavings(FamilyProfile familyProfile,
                                             DashboardSummaryDTO.EducationPlanSummary educationSummary,
                                             DashboardSummaryDTO.MarriagePlanSummary marriageSummary) {
        BigDecimal monthlySavings = familyProfile.getMonthlyIncome().subtract(familyProfile.getMonthlyExpenses());
        BigDecimal goalsSavings = educationSummary.getTotalEducationSavings()
                .add(marriageSummary.getTotalMarriageSavings());
        return monthlySavings.add(goalsSavings);
    }

    private Double calculateSavingsGrowth(Long familyProfileId) {
        // Implementation for calculating savings growth percentage over time
        // This would typically compare current month vs previous month
        return 5.2; // Placeholder
    }

    private Double calculateInvestmentGrowth(Long familyProfileId) {
        // Implementation for calculating investment growth percentage
        return 8.7; // Placeholder
    }

    private Double calculateTotalGoalsProgress(DashboardSummaryDTO.EducationPlanSummary educationSummary,
                                               DashboardSummaryDTO.MarriagePlanSummary marriageSummary) {
        Double educationProgress = educationSummary.getEducationGoalsProgress();
        Double marriageProgress = marriageSummary.getMarriageGoalsProgress();
        return (educationProgress + marriageProgress) / 2.0;
    }

    private Double calculatePortfolioGrowth(List<InvestmentPlanDTO> investmentPlans) {
        if (investmentPlans.isEmpty()) return 0.0;

        // Fix: Use lambda expression to convert BigDecimal to double
        return investmentPlans.stream()
                .mapToDouble(plan -> plan.getExpectedReturn().doubleValue())
                .average()
                .orElse(0.0);
    }


    private String getNextEducationMilestone(List<EducationPlanDTO> plans) {
        return plans.stream()
                .findFirst()
                .map(plan -> plan.getChildName() + " - " + plan.getEducationLevel())
                .orElse("No education milestones");
    }

    private String getNextMarriageMilestone(List<MarriagePlanDTO> plans) {
        return plans.stream()
                .findFirst()
                .map(plan -> plan.getForName() + " Marriage")
                .orElse("No marriage milestones");
    }
}
