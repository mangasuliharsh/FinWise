package com.finwise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationPlanProgressDTO {
    private Long id;
    private String planName;
    private String educationLevel;
    private String institutionType;
    private Integer estimatedStartYear;
    private Integer estimatedEndYear;
    private BigDecimal estimatedTotalCost;
    private BigDecimal currentSavings;
    private BigDecimal monthlyContribution;
    private BigDecimal inflationRate;
    private String notes;
    private String childName;
    private String childEducationLevel;

    // Calculated fields
    private Double progressPercentage;
    private Long daysRemaining;
    private String status;
    private BigDecimal monthlyRequiredSaving;
    private BigDecimal inflationAdjustedCost;
    private BigDecimal futureValue;
    private BigDecimal shortfall;

    // getters and setters
}
