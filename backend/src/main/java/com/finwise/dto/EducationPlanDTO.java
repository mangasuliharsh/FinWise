package com.finwise.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationPlanDTO {

    private Long id;

    @NotBlank(message = "Plan name must not be blank")
    private String planName;

    @NotBlank(message = "Education level must not be blank")
    private String educationLevel;

    @NotBlank(message = "Institution type must not be blank")
    private String institutionType;

    @Min(value = 1900, message = "Estimated start year must be no earlier than 1900")
    @Max(value = 2100, message = "Estimated start year must be no later than 2100")
    private int estimatedStartYear;

    @Min(value = 1900, message = "Estimated end year must be no earlier than 1900")
    @Max(value = 2100, message = "Estimated end year must be no later than 2100")
    private int estimatedEndYear;

    @NotNull(message = "Estimated total cost must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Estimated total cost must be positive")
    private BigDecimal estimatedTotalCost;

    @NotNull(message = "Current savings must not be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Current savings cannot be negative")
    private BigDecimal currentSavings;

    @NotNull(message = "Monthly contribution must not be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Monthly contribution cannot be negative")
    private BigDecimal monthlyContribution;

    @NotNull(message = "Inflation rate must not be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Inflation rate cannot be negative")
    private BigDecimal inflationRate;

    private String notes;

    @NotNull(message = "Child ID must be provided")
    private Long childId;  // Reference to Child entity's ID
}
