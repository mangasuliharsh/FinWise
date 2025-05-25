package com.finwise.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentDTO {

    private Long id;

    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Start date must not be null")
    private LocalDate startDate;

    @NotNull(message = "Maturity date must not be null")
    private LocalDate maturityDate;

    @NotNull(message = "Current value must not be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Current value cannot be negative")
    private BigDecimal currentValue;

    @NotBlank(message = "Status must not be blank")
    private String status;

    @NotNull(message = "Savings plan ID must be provided")
    private Long savingsPlanId;

    @NotNull(message = "Investment option ID must be provided")
    private Long investmentOptionId;
}
