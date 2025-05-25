package com.finwise.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MarriagePlanDTO {

    private Long id;

    @NotBlank(message = "Plan name is required")
    private String planName;

    @NotBlank(message = "Name of the person the plan is for is required")
    private String forName;

    @NotBlank(message = "Relationship is required")
    private String relationship;

    @Min(value = 2024, message = "Estimated year must be current year or later")
    private int estimatedYear;

    @NotNull(message = "Estimated total cost is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Estimated total cost must be greater than zero")
    private BigDecimal estimatedTotalCost;

    @DecimalMin(value = "0.0", message = "Current savings cannot be negative")
    private BigDecimal currentSavings = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Monthly contribution cannot be negative")
    private BigDecimal monthlyContribution = BigDecimal.ZERO;

    @NotNull(message = "Inflation rate is required")
    @DecimalMin(value = "0.0", message = "Inflation rate cannot be negative")
    private BigDecimal inflationRate = new BigDecimal("6.00");

    private String notes;

    @NotNull(message = "Family Profile ID is required")
    private Long familyProfileId;
}
