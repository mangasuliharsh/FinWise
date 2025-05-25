package com.finwise.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "marriage_expense_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarriageExpenseCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoryName;
    private BigDecimal estimatedCost;
    private String priority = "MEDIUM";
    private String notes;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime lastUpdatedDate;

    @ManyToOne
    @JoinColumn(name = "marriage_plan_id", nullable = false)
    private MarriagePlan marriagePlan;
}
