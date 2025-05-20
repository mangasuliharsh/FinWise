package com.finwise.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "financial_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionType;
    private BigDecimal amount;
    private String description;
    private Long relatedPlanId;
    private String relatedPlanType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

