package com.finwise.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Report type is required")
    @Pattern(regexp = "MONTHLY|QUARTERLY|ANNUAL|CUSTOM",
            message = "Report type must be MONTHLY, QUARTERLY, ANNUAL, or CUSTOM")
    @Size(max = 50, message = "Report type must not exceed 50 characters")
    private String reportType;

    @NotBlank(message = "Report data is required")
    @Size(max = 10000, message = "Report data must not exceed 10000 characters")
    private String reportData; // JSON string containing the report data

    private LocalDateTime generatedDate;
}
