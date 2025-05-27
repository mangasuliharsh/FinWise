package com.finwise.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationPlanDTO {

    private Long id;

    @NotBlank(message = "Plan name must not be blank")
    @Size(max = 255, message = "Plan name must not exceed 255 characters")
    private String planName;

    @NotBlank(message = "Education level must not be blank")
    @Size(max = 100, message = "Education level must not exceed 100 characters")
    private String educationLevel;

    @NotBlank(message = "Institution type must not be blank")
    @Size(max = 100, message = "Institution type must not exceed 100 characters")
    private String institutionType;

    @NotNull(message = "Estimated start year must not be null")
    @Min(value = 1900, message = "Estimated start year must be no earlier than 1900")
    @Max(value = 2100, message = "Estimated start year must be no later than 2100")
    private Integer estimatedStartYear;

    @NotNull(message = "Estimated end year must not be null")
    @Min(value = 1900, message = "Estimated end year must be no earlier than 1900")
    @Max(value = 2100, message = "Estimated end year must be no later than 2100")
    private Integer estimatedEndYear;

    @NotNull(message = "Estimated total cost must not be null")
    @DecimalMin(value = "0.01", message = "Estimated total cost must be positive")
    @Digits(integer = 15, fraction = 2, message = "Estimated total cost format is invalid")
    private BigDecimal estimatedTotalCost;

    @NotNull(message = "Current savings must not be null")
    @DecimalMin(value = "0.00", inclusive = true, message = "Current savings cannot be negative")
    @Digits(integer = 15, fraction = 2, message = "Current savings format is invalid")
    private BigDecimal currentSavings;

    @NotNull(message = "Monthly contribution must not be null")
    @DecimalMin(value = "0.00", inclusive = true, message = "Monthly contribution cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Monthly contribution format is invalid")
    private BigDecimal monthlyContribution;

    @NotNull(message = "Inflation rate must not be null")
    @DecimalMin(value = "0.00", inclusive = true, message = "Inflation rate cannot be negative")
    @DecimalMax(value = "99.99", inclusive = true, message = "Inflation rate cannot exceed 99.99%")
    @Digits(integer = 2, fraction = 2, message = "Inflation rate must have at most 2 digits before and 2 digits after decimal point")
    private BigDecimal inflationRate;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedDate;

    @NotNull(message = "Child ID must be provided")
    private Long childId;

    // Optional: Child information for display purposes
    private String childName;
    private String childEducationLevel;

    // Custom validation method
    @AssertTrue(message = "End year must be after start year")
    public boolean isValidYearRange() {
        if (estimatedStartYear == null || estimatedEndYear == null) {
            return true; // Let @NotNull handle null validation
        }
        return estimatedEndYear > estimatedStartYear;
    }

    // Helper methods for calculations (optional)
    public BigDecimal calculateInflationAdjustedCost(int targetYear) {
        if (estimatedTotalCost == null || inflationRate == null) {
            return BigDecimal.ZERO;
        }

        int years = targetYear - (estimatedStartYear != null ? estimatedStartYear : 0);
        if (years <= 0) {
            return estimatedTotalCost;
        }

        double rate = inflationRate.doubleValue() / 100.0;
        double multiplier = Math.pow(1 + rate, years);
        return estimatedTotalCost.multiply(BigDecimal.valueOf(multiplier));
    }

    public BigDecimal calculateFutureValue(int targetYear) {
        if (currentSavings == null || monthlyContribution == null || inflationRate == null) {
            return BigDecimal.ZERO;
        }

        int years = targetYear - (estimatedStartYear != null ? estimatedStartYear : 0);
        if (years <= 0) {
            return currentSavings;
        }

        double rate = inflationRate.doubleValue() / 100.0;
        double monthlyRate = rate / 12.0;
        int months = years * 12;

        // Future value of current savings
        double futureCurrentSavings = currentSavings.doubleValue() * Math.pow(1 + rate, years);

        // Future value of monthly contributions (annuity)
        double futureMonthlyContributions = 0;
        if (monthlyRate > 0) {
            futureMonthlyContributions = monthlyContribution.doubleValue() *
                    ((Math.pow(1 + monthlyRate, months) - 1) / monthlyRate);
        } else {
            futureMonthlyContributions = monthlyContribution.doubleValue() * months;
        }

        return BigDecimal.valueOf(futureCurrentSavings + futureMonthlyContributions);
    }

    public BigDecimal calculateShortfall(int targetYear) {
        BigDecimal futureValue = calculateFutureValue(targetYear);
        BigDecimal inflationAdjustedCost = calculateInflationAdjustedCost(targetYear);

        BigDecimal shortfall = inflationAdjustedCost.subtract(futureValue);
        return shortfall.compareTo(BigDecimal.ZERO) > 0 ? shortfall : BigDecimal.ZERO;
    }

    public double calculateProgressPercentage(int targetYear) {
        BigDecimal futureValue = calculateFutureValue(targetYear);
        BigDecimal inflationAdjustedCost = calculateInflationAdjustedCost(targetYear);

        if (inflationAdjustedCost.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }

        double progress = futureValue.divide(inflationAdjustedCost, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();

        return Math.min(100.0, Math.max(0.0, progress));
    }
}
