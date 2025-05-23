package com.finwise.models;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class DashboardSummary {
    private FinancialSummary financialSummary;
    private List<SavingsProgress> savingsProgress;
    private List<Transaction> recentTransactions;
    private List<Milestone> upcomingMilestones;
    private List<FinancialAlert> financialAlerts;

    @Data
    public static class FinancialSummary {
        private BigDecimal totalSavings;
        private BigDecimal educationFunds;
        private BigDecimal marriageFunds;
        private BigDecimal emergencyFunds;
        private BigDecimal investmentFunds;
        private BigDecimal monthlyContribution;
    }

    @Data
    public static class SavingsProgress {
        private String type;
        private BigDecimal target;
        private BigDecimal current;
        private double percentage;
    }

    @Data
    public static class Transaction {
        private String description;
        private BigDecimal amount;
        private String category;
        private String date;
    }

    @Data
    public static class Milestone {
        private String name;
        private String type;
        private BigDecimal amount;
        private String date;
    }

    @Data
    public static class FinancialAlert {
        private String message;
        private String severity;
    }
}
