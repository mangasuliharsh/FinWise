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
public class MarriageCostReferenceDTO {

    private Long id;

    @NotBlank(message = "Region is required")
    @Size(max = 100, message = "Region must not exceed 100 characters")
    private String region;

    @NotBlank(message = "Wedding type is required")
    @Size(max = 50, message = "Wedding type must not exceed 50 characters")
    private String weddingType;

    @NotNull(message = "Average cost is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Average cost must be positive")
    private BigDecimal averageCost;

    @NotNull(message = "Minimum cost is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Minimum cost must be positive")
    private BigDecimal minimumCost;

    @NotNull(message = "Maximum cost is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Maximum cost must be positive")
    private BigDecimal maximumCost;

    @NotNull(message = "Reference year is required")
    @Min(value = 2020, message = "Reference year must be 2020 or later")
    private Integer referenceYear;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedDate;
}
