package com.finwise.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FamilyProfileDTO {

    private Long id;

    @Min(value = 1, message = "Family size must be at least 1")
    private int familySize;

    @NotNull(message = "Monthly income must not be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Monthly income cannot be negative")
    private BigDecimal monthlyIncome;

    @NotNull(message = "Monthly expenses must not be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Monthly expenses cannot be negative")
    private BigDecimal monthlyExpenses;

    @NotBlank(message = "Location must not be blank")
    private String location;

    @NotBlank(message = "Risk tolerance must not be blank")
    private String riskTolerance;


    private Long userId;

    private List<Long> childrenIds;

    private List<Long> marriagePlanIds;

    private List<Long> savingsPlanIds;
}
