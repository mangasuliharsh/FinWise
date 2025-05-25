package com.finwise.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarriageExpenseCategoryDTO {

    private Long id;

    @NotNull(message = "Marriage plan ID is required")
    private Long marriagePlanId;

    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name must not exceed 100 characters")
    private String categoryName;

    @NotNull(message = "Estimated cost is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Estimated cost must be positive")
    private BigDecimal estimatedCost;

    @NotNull(message = "Priority level is required")
    @Min(value = 1, message = "Priority level must be at least 1")
    @Max(value = 10, message = "Priority level cannot exceed 10")
    private Integer priorityLevel;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedDate;
}
