package com.finwise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentPlanProgressDTO {
    private Long id;
    private String planName;
    private BigDecimal goalAmount;
    private BigDecimal currentSavings;
    private BigDecimal monthlyContribution;
    private BigDecimal expectedReturn;
    private Integer targetYear;
    private String notes;

    // Calculated fields
    private Double progressPercentage;
    private Long daysRemaining;
    private String status;
    private BigDecimal projectedValue;
    private BigDecimal shortfall;
    private BigDecimal totalContributions;
    private BigDecimal expectedGains;

    // getters and setters
}
