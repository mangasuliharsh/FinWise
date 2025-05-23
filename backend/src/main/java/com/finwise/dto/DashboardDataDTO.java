package com.finwise.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class DashboardDataDTO {
    private FinancialSummaryDTO financialSummary;
    private List<MilestoneDTO> upcomingMilestones;
    private SavingsProgressDTO savingsProgress;
    private List<TransactionDTO> recentTransactions;
    private List<AlertDTO> financialAlerts;

    @Data
    public static class FinancialSummaryDTO {
        private BigDecimal totalSavings;
        private BigDecimal educationFunds;
        private BigDecimal marriageFunds;
        private BigDecimal emergencyFunds;
        private BigDecimal investmentFunds;
        private BigDecimal monthlyContribution;
    }

    @Data
    public static class MilestoneDTO {
        private String name;
        private String type;
        private BigDecimal amount;
        private String date;
    }

    @Data
    public static class SavingsProgressDTO {
        private ProgressDTO education;
        private ProgressDTO marriage;
        private ProgressDTO emergency;
    }

    @Data
    public static class ProgressDTO {
        private BigDecimal target;
        private BigDecimal current;
    }

    @Data
    public static class TransactionDTO {
        private String description;
        private String category;
        private BigDecimal amount;
        private String date;
    }

    @Data
    public static class AlertDTO {
        private String message;
        private String severity;
    }
}
