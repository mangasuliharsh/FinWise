package com.finwise.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentOptionDTO {

    private Long id;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Type must not be blank")
    private String type;

    @NotBlank(message = "Risk level must not be blank")
    private String riskLevel;

    @NotNull(message = "Expected annual return must not be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Expected annual return cannot be negative")
    private BigDecimal expectedAnnualReturn;

    @NotNull(message = "Minimum investment period must not be null")
    @Min(value = 0, message = "Minimum investment period cannot be negative")
    private Integer minInvestmentPeriod;

    @NotBlank(message = "Description must not be blank")
    private String description;

    private boolean isActive = true;
}
