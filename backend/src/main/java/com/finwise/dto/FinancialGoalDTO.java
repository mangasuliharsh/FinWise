package com.finwise.dto;

import lombok.Data;

@Data
public class FinancialGoalDTO {
    private String goalName;
    private long currentAmount;
    private long targetAmount;
    private int progressPercent; // e.g. 50 for 50%
}