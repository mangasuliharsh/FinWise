package com.finwise.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialTransactionDTO {

    private Long id;

    @NotBlank(message = "Transaction type is required")
    private String transactionType;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    private BigDecimal amount;

    @Size(max = 255, message = "Description should not exceed 255 characters")
    private String description;

    private Long relatedPlanId;

    private String relatedPlanType;

//    @NotNull(message = "User ID is required")
    private Long userId;
}
