package com.finwise.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EconomicIndicatorDTO {

    private Long id;

    @NotBlank(message = "Indicator name is required")
    private String indicatorName;

    @NotNull(message = "Value is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Value must be greater than 0")
    private BigDecimal value;

    @Min(value = 1900, message = "Year must be after 1900")
    @Max(value = 2100, message = "Year must be before 2100")
    private int year;

    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private Integer month;

    @NotBlank(message = "Data source is required")
    private String dataSource;
}
