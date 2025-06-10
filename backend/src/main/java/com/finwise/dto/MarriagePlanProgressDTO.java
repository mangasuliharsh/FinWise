package com.finwise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarriagePlanProgressDTO {
    private Long id;
    private String planName;
    private String forName;
    private String relationship;
    private Integer estimatedYear;
    private BigDecimal estimatedTotalCost;
    private BigDecimal currentSavings;
    private BigDecimal monthlyContribution;
    private BigDecimal inflationRate;
    private String notes;

    // Calculated fields
    private Double progressPercentage;
    private Long daysRemaining;
    private String status; // ON_TRACK, BEHIND, AHEAD, COMPLETED
    private BigDecimal monthlyRequiredSaving;
    private BigDecimal inflationAdjustedCost;
    private BigDecimal shortfall;

    // getters and setters
}