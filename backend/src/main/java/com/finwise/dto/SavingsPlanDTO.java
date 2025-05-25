package com.finwise.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavingsPlanDTO {

    private Long id;

    @NotNull(message = "Family profile ID is required")
    private Long familyProfileId;

    @NotBlank(message = "Plan name is required")
    @Size(max = 100, message = "Plan name must not exceed 100 characters")
    private String planName;

    @NotNull(message = "Target amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Target amount must be positive")
    private BigDecimal targetAmount;

    @NotNull(message = "Current amount is required")
    @DecimalMin(value = "0.0", message = "Current amount cannot be negative")
    private BigDecimal currentAmount;

    @NotNull(message = "Monthly contribution is required")
    @DecimalMin(value = "0.0", message = "Monthly contribution cannot be negative")
    private BigDecimal monthlyContribution;

    @NotNull(message = "Target date is required")
    @Future(message = "Target date must be in the future")
    private LocalDate targetDate;

    @Size(max = 200, message = "Purpose must not exceed 200 characters")
    private String purpose;

    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedDate;
}
