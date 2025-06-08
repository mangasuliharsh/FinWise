package com.finwise.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PlanTransactionDTO {
    private Long familyProfileId;   // The user's family profile
    private String planType;        // "child", "education", "investment"
    private Long planId;            // The specific plan's ID
    private String action;          // "add" or "remove"
    private BigDecimal amount;      // Amount to add/remove
    private String description;     // Optional description (e.g. "Added funds for school fee")
    private LocalDateTime dateTime; // (Optional) When the transaction was made
}
