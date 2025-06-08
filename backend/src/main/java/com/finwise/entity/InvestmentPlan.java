package com.finwise.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "investment_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String planName;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal goalAmount;

    @Column(precision = 15, scale = 2)
    private BigDecimal currentSavings = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal monthlyContribution = BigDecimal.ZERO;

    @Column(precision = 5, scale = 2)
    private BigDecimal expectedReturn = new BigDecimal("8.00");

    @Column(nullable = false)
    private Integer targetYear;

    @Column(length = 500)
    private String notes;

    @Column(nullable = false)
    private Long familyProfileId;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedDate;
}
