package com.finwise.dto;

import lombok.Data;
import java.util.List;

@Data
public class DashboardMetricsDTO {
    private long totalSavings;
    private long monthlyIncome;
    private long monthlyExpenses;
    private long investments;
    private List<FinancialGoalDTO> goals;
    private List<RecentActivityDTO> recentTransactions;
}