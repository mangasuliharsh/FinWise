package com.finwise.util;

import com.finwise.dto.DashboardSummaryDTO;
import com.finwise.entity.FamilyProfile;
import org.springframework.stereotype.Component;

@Component
public class DashboardMapper {

    public DashboardSummaryDTO mapToSummaryDto(FamilyProfile familyProfile) {
        return DashboardSummaryDTO.builder()
                .monthlyIncome(familyProfile.getMonthlyIncome())
                .monthlyExpenses(familyProfile.getMonthlyExpenses())
                .build();
    }

    public DashboardSummaryDTO.EducationPlanSummary mapToEducationSummary(
            int totalPlans,
            java.math.BigDecimal totalCost,
            java.math.BigDecimal totalSavings) {
        return DashboardSummaryDTO.EducationPlanSummary.builder()
                .totalPlans(totalPlans)
                .totalEducationCost(totalCost)
                .totalEducationSavings(totalSavings)
                .build();
    }

    public DashboardSummaryDTO.MarriagePlanSummary mapToMarriageSummary(
            int totalPlans,
            java.math.BigDecimal totalCost,
            java.math.BigDecimal totalSavings) {
        return DashboardSummaryDTO.MarriagePlanSummary.builder()
                .totalPlans(totalPlans)
                .totalMarriageCost(totalCost)
                .totalMarriageSavings(totalSavings)
                .build();
    }

    public DashboardSummaryDTO.InvestmentPortfolioSummary mapToInvestmentSummary(
            int totalPlans,
            java.math.BigDecimal totalValue,
            java.math.BigDecimal totalGains) {
        return DashboardSummaryDTO.InvestmentPortfolioSummary.builder()
                .totalPlans(totalPlans)
                .totalPortfolioValue(totalValue)
                .totalGains(totalGains)
                .build();
    }
}
