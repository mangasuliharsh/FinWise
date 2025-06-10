package com.finwise.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OverallSummaryDTO {

    private BigDecimal totalMarriageSavings;
    private BigDecimal totalEducationSavings;
    private BigDecimal totalInvestmentValue;
    private BigDecimal totalPortfolioValue;

    private Integer totalMarriagePlans;
    private Integer totalEducationPlans;
    private Integer totalInvestmentPlans;

    private Integer completedPlans;
    private Integer onTrackPlans;
    private Integer behindPlans;
    private Integer aheadPlans;

    private Double overallProgressPercentage;
    private BigDecimal monthlyTotalContributions;
}

