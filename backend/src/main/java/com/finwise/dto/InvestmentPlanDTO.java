package com.finwise.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentPlanDTO {

    private Long id;

    @NotBlank(message = "Plan name must not be blank")
    @Size(max = 100, message = "Plan name must not exceed 100 characters")
    private String planName;

    @NotNull(message = "Goal amount must not be null")
    @DecimalMin(value = "0.01", message = "Goal amount must be greater than zero")
    private BigDecimal goalAmount;

    @DecimalMin(value = "0.0", message = "Current savings cannot be negative")
    private BigDecimal currentSavings = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Monthly contribution cannot be negative")
    private BigDecimal monthlyContribution = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Expected return cannot be negative")
    @DecimalMax(value = "100.0", message = "Expected return cannot exceed 100%")
    private BigDecimal expectedReturn = new BigDecimal("8.00");

    @NotNull(message = "Target year must not be null")
    @Min(value = 2024, message = "Target year must be current year or later")
    private Integer targetYear;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;

    @NotNull(message = "Family profile ID must be provided")
    private Long familyProfileId;
}
