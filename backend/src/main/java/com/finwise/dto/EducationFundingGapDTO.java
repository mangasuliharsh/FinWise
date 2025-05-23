package com.finwise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationFundingGapDTO {
    private BigDecimal estimatedTotalCost;
    private BigDecimal currentSavings;
    private BigDecimal projectedSavings;
    private BigDecimal monthlyContribution;
    private BigDecimal fundingGap;
    private int monthsUntilStart;
    private String planName;
    private String educationLevel;
}
