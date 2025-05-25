package com.finwise.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationCostReferenceDTO {

    private Long id;

    @NotBlank(message = "Education level must not be blank")
    private String educationLevel;

    @NotBlank(message = "Institution type must not be blank")
    private String institutionType;

    @NotBlank(message = "Region must not be blank")
    private String region;

    @NotNull(message = "Average annual cost must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Average annual cost must be positive")
    private BigDecimal averageAnnualCost;

    @Min(value = 1900, message = "Year must be no earlier than 1900")
    @Max(value = 2100, message = "Year must be no later than 2100")
    private int year;

    @NotBlank(message = "Data source must not be blank")
    private String dataSource;
}
