package com.finwise.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardSummaryDTO {

    // Financial Overview
    @JsonProperty("totalSavings")
    private BigDecimal totalSavings;

    @JsonProperty("monthlyIncome")
    private BigDecimal monthlyIncome;

    @JsonProperty("monthlyExpenses")
    private BigDecimal monthlyExpenses;

    @JsonProperty("totalInvestments")
    private BigDecimal totalInvestments;

    // Education Planning Summary
    @JsonProperty("educationPlanSummary")
    private EducationPlanSummary educationPlanSummary;

    // Marriage Planning Summary
    @JsonProperty("marriagePlanSummary")
    private MarriagePlanSummary marriagePlanSummary;

    // Investment Portfolio Summary
    @JsonProperty("investmentPortfolioSummary")
    private InvestmentPortfolioSummary investmentPortfolioSummary;

    // Recent Transactions
    @JsonProperty("recentTransactions")
    private List<RecentTransactionSummary> recentTransactions;

    // Growth Percentages
    @JsonProperty("savingsGrowthPercentage")
    private Double savingsGrowthPercentage;

    @JsonProperty("investmentGrowthPercentage")
    private Double investmentGrowthPercentage;

    @JsonProperty("totalGoalsProgress")
    private Double totalGoalsProgress;

    // Nested DTOs for specific plan summaries
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EducationPlanSummary {
        private Integer totalPlans;
        private BigDecimal totalEducationCost;
        private BigDecimal totalEducationSavings;
        private BigDecimal monthlyEducationContribution;
        private Double educationGoalsProgress;
        private Integer activePlans;
        private String nextMilestone;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MarriagePlanSummary {
        private Integer totalPlans;
        private BigDecimal totalMarriageCost;
        private BigDecimal totalMarriageSavings;
        private BigDecimal monthlyMarriageContribution;
        private Double marriageGoalsProgress;
        private Integer activePlans;
        private String nextMilestone;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InvestmentPortfolioSummary {
        private Integer totalPlans;
        private BigDecimal totalPortfolioValue;
        private BigDecimal totalGains;
        private BigDecimal monthlyInvestmentContribution;
        private Double portfolioGrowthPercentage;
        private String bestPerformingPlan;
        private Double bestPerformingGrowth;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecentTransactionSummary {
        private Long id;
        private String planType;
        private String action;
        private BigDecimal amount;
        private String description;
        private String date;
        private String planName;
    }

    // Calculated fields
    public Double getSavingsRate() {
        if (monthlyIncome != null && monthlyIncome.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal monthlySavings = monthlyIncome.subtract(monthlyExpenses);
            return monthlySavings.divide(monthlyIncome, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
        }
        return 0.0;
    }

    public BigDecimal getTotalMonthlySavingsAllocation() {
        BigDecimal educationContribution = educationPlanSummary != null ?
                educationPlanSummary.getMonthlyEducationContribution() : BigDecimal.ZERO;
        BigDecimal marriageContribution = marriagePlanSummary != null ?
                marriagePlanSummary.getMonthlyMarriageContribution() : BigDecimal.ZERO;
        BigDecimal investmentContribution = investmentPortfolioSummary != null ?
                investmentPortfolioSummary.getMonthlyInvestmentContribution() : BigDecimal.ZERO;

        return educationContribution.add(marriageContribution).add(investmentContribution);
    }

    public Double getOverallFinancialHealth() {
        double savingsRate = getSavingsRate();
        double goalsProgress = totalGoalsProgress != null ? totalGoalsProgress : 0.0;
        double investmentGrowth = investmentGrowthPercentage != null ? investmentGrowthPercentage : 0.0;

        return (savingsRate * 0.4) + (goalsProgress * 0.4) + (investmentGrowth * 0.2);
    }
}
