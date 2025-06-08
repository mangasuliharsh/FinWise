package com.finwise.dto;

import lombok.Data;

@Data
public class RecentActivityDTO {
    private String description;
    private String type; // e.g. "credit", "debit", "investment"
    private long amount;
    private String dateTime; // Format as needed for frontend
}