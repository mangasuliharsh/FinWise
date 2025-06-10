package com.finwise.service;

import com.finwise.dto.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ComprehensiveReportService {

    private final FamilyProfileService familyProfileService;
    private final MarriagePlanService marriagePlanService;
    private final EducationPlanService educationPlanService;
    private final InvestmentPlanService investmentPlanService;
    private final ModelMapper modelMapper;

    public ComprehensiveReportDTO generateComprehensiveReport(Long userId) {
        ComprehensiveReportDTO report = new ComprehensiveReportDTO();
        report.setUserId(userId);
        report.setReportGeneratedAt(LocalDateTime.now());

        // Get family profile
        FamilyProfileDTO familyProfile = familyProfileService.getProfileByUserId(userId);
        report.setFamilyProfile(familyProfile);

        // Get all plans progress
        List<MarriagePlanProgressDTO> marriagePlans = calculateMarriagePlansProgress(userId);
        List<EducationPlanProgressDTO> educationPlans = calculateEducationPlansProgress(userId);
        List<InvestmentPlanProgressDTO> investmentPlans = calculateInvestmentPlansProgress(userId);

        report.setMarriagePlans(marriagePlans);
        report.setEducationPlans(educationPlans);
        report.setInvestmentPlans(investmentPlans);

        // Calculate overall summary
        OverallSummaryDTO summary = calculateOverallSummary(marriagePlans, educationPlans, investmentPlans);
        report.setOverallSummary(summary);

        return report;
    }

    public List<MarriagePlanProgressDTO> calculateMarriagePlansProgress(Long userId) {
        List<MarriagePlanDTO> plans = marriagePlanService.getPlansByUserId(userId);

        return plans.stream().map(plan -> {
            MarriagePlanProgressDTO progress = modelMapper.map(plan, MarriagePlanProgressDTO.class);

            // Calculate inflation-adjusted cost
            int yearsToMarriage = plan.getEstimatedYear() - LocalDate.now().getYear();
            BigDecimal inflationAdjustedCost = calculateInflationAdjustedAmount(
                    plan.getEstimatedTotalCost(),
                    plan.getInflationRate(),
                    yearsToMarriage
            );
            progress.setInflationAdjustedCost(inflationAdjustedCost);

            // Calculate progress percentage
            double progressPercentage = plan.getCurrentSavings()
                    .divide(inflationAdjustedCost, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
            progress.setProgressPercentage(Math.min(progressPercentage, 100.0));

            // Calculate days remaining
            LocalDate targetDate = LocalDate.of(plan.getEstimatedYear(), 6, 1); // Assume mid-year
            long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), targetDate);
            progress.setDaysRemaining(Math.max(daysRemaining, 0));

            // Calculate required monthly saving
            if (daysRemaining > 0) {
                BigDecimal remainingAmount = inflationAdjustedCost.subtract(plan.getCurrentSavings());
                double monthsRemaining = daysRemaining / 30.0;
                if (monthsRemaining > 0) {
                    progress.setMonthlyRequiredSaving(
                            remainingAmount.divide(BigDecimal.valueOf(monthsRemaining), 2, RoundingMode.HALF_UP)
                    );
                }
            }

            // Calculate shortfall
            BigDecimal shortfall = inflationAdjustedCost.subtract(plan.getCurrentSavings());
            progress.setShortfall(shortfall.compareTo(BigDecimal.ZERO) > 0 ? shortfall : BigDecimal.ZERO);

            // Determine status
            progress.setStatus(determineStatus(progressPercentage, daysRemaining));

            return progress;
        }).collect(Collectors.toList());
    }

    public List<EducationPlanProgressDTO> calculateEducationPlansProgress(Long userId) {
        List<EducationPlanDTO> plans = educationPlanService.getPlansByUserId(userId);

        return plans.stream().map(plan -> {
            EducationPlanProgressDTO progress = modelMapper.map(plan, EducationPlanProgressDTO.class);

            // Use the built-in calculation methods from DTO
            int targetYear = plan.getEstimatedStartYear();

            BigDecimal inflationAdjustedCost = plan.calculateInflationAdjustedCost(targetYear);
            BigDecimal futureValue = plan.calculateFutureValue(targetYear);
            BigDecimal shortfall = plan.calculateShortfall(targetYear);
            double progressPercentage = plan.calculateProgressPercentage(targetYear);

            progress.setInflationAdjustedCost(inflationAdjustedCost);
            progress.setFutureValue(futureValue);
            progress.setShortfall(shortfall);
            progress.setProgressPercentage(progressPercentage);

            // Calculate days remaining
            LocalDate targetDate = LocalDate.of(targetYear, 6, 1);
            long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), targetDate);
            progress.setDaysRemaining(Math.max(daysRemaining, 0));

            // Calculate required monthly saving
            if (daysRemaining > 0 && shortfall.compareTo(BigDecimal.ZERO) > 0) {
                double monthsRemaining = daysRemaining / 30.0;
                if (monthsRemaining > 0) {
                    progress.setMonthlyRequiredSaving(
                            shortfall.divide(BigDecimal.valueOf(monthsRemaining), 2, RoundingMode.HALF_UP)
                    );
                }
            }

            progress.setStatus(determineStatus(progressPercentage, daysRemaining));

            return progress;
        }).collect(Collectors.toList());
    }

    public List<InvestmentPlanProgressDTO> calculateInvestmentPlansProgress(Long userId) {
        List<InvestmentPlanDTO> plans = investmentPlanService.getPlansByUserId(userId);

        return plans.stream().map(plan -> {
            InvestmentPlanProgressDTO progress = modelMapper.map(plan, InvestmentPlanProgressDTO.class);

            // Calculate projected value using compound interest
            int yearsToTarget = plan.getTargetYear() - LocalDate.now().getYear();
            BigDecimal projectedValue = calculateFutureValueWithReturns(
                    plan.getCurrentSavings(),
                    plan.getMonthlyContribution(),
                    plan.getExpectedReturn(),
                    yearsToTarget
            );
            progress.setProjectedValue(projectedValue);

            // Calculate progress percentage
            double progressPercentage = projectedValue
                    .divide(plan.getGoalAmount(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
            progress.setProgressPercentage(Math.min(progressPercentage, 100.0));

            // Calculate days remaining
            LocalDate targetDate = LocalDate.of(plan.getTargetYear(), 12, 31);
            long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), targetDate);
            progress.setDaysRemaining(Math.max(daysRemaining, 0));

            // Calculate shortfall
            BigDecimal shortfall = plan.getGoalAmount().subtract(projectedValue);
            progress.setShortfall(shortfall.compareTo(BigDecimal.ZERO) > 0 ? shortfall : BigDecimal.ZERO);

            // Calculate total contributions and expected gains
            BigDecimal totalContributions = plan.getCurrentSavings()
                    .add(plan.getMonthlyContribution().multiply(BigDecimal.valueOf(yearsToTarget * 12)));
            progress.setTotalContributions(totalContributions);

            BigDecimal expectedGains = projectedValue.subtract(totalContributions);
            progress.setExpectedGains(expectedGains);

            progress.setStatus(determineStatus(progressPercentage, daysRemaining));

            return progress;
        }).collect(Collectors.toList());
    }

    private BigDecimal calculateInflationAdjustedAmount(BigDecimal amount, BigDecimal inflationRate, int years) {
        if (years <= 0) return amount;

        double rate = inflationRate.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP).doubleValue();
        double multiplier = Math.pow(1 + rate, years);
        return amount.multiply(BigDecimal.valueOf(multiplier)).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateFutureValueWithReturns(BigDecimal currentAmount, BigDecimal monthlyContribution,
                                                       BigDecimal expectedReturn, int years) {
        if (years <= 0) return currentAmount;

        double annualRate = expectedReturn.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP).doubleValue();
        double monthlyRate = annualRate / 12;
        int months = years * 12;

        // Future value of current amount
        double futureCurrentAmount = currentAmount.doubleValue() * Math.pow(1 + annualRate, years);

        // Future value of monthly contributions (annuity)
        double futureMonthlyContributions = 0;
        if (monthlyRate > 0) {
            futureMonthlyContributions = monthlyContribution.doubleValue() *
                    ((Math.pow(1 + monthlyRate, months) - 1) / monthlyRate);
        } else {
            futureMonthlyContributions = monthlyContribution.doubleValue() * months;
        }

        return BigDecimal.valueOf(futureCurrentAmount + futureMonthlyContributions)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private String determineStatus(double progressPercentage, long daysRemaining) {
        if (progressPercentage >= 100) return "COMPLETED";
        if (daysRemaining <= 0) return "OVERDUE";

        // Calculate expected progress based on time elapsed
        // This is a simplified calculation - you might want to make it more sophisticated
        double timeProgress = Math.max(0, 100 - (daysRemaining / 365.0 * 100));

        if (progressPercentage >= timeProgress * 1.1) return "AHEAD";
        if (progressPercentage >= timeProgress * 0.9) return "ON_TRACK";
        return "BEHIND";
    }

    private OverallSummaryDTO calculateOverallSummary(List<MarriagePlanProgressDTO> marriagePlans,
                                                      List<EducationPlanProgressDTO> educationPlans,
                                                      List<InvestmentPlanProgressDTO> investmentPlans) {
        OverallSummaryDTO summary = new OverallSummaryDTO();

        // Calculate totals
        BigDecimal totalMarriageSavings = marriagePlans.stream()
                .map(MarriagePlanProgressDTO::getCurrentSavings)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalEducationSavings = educationPlans.stream()
                .map(EducationPlanProgressDTO::getCurrentSavings)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalInvestmentValue = investmentPlans.stream()
                .map(InvestmentPlanProgressDTO::getCurrentSavings)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        summary.setTotalMarriageSavings(totalMarriageSavings);
        summary.setTotalEducationSavings(totalEducationSavings);
        summary.setTotalInvestmentValue(totalInvestmentValue);
        summary.setTotalPortfolioValue(totalMarriageSavings.add(totalEducationSavings).add(totalInvestmentValue));

        // Count plans by status
        summary.setTotalMarriagePlans(marriagePlans.size());
        summary.setTotalEducationPlans(educationPlans.size());
        summary.setTotalInvestmentPlans(investmentPlans.size());

        return summary;
    }
}
