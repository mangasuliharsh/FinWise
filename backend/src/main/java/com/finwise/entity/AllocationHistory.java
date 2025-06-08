package com.finwise.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "allocation_history")
public class AllocationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "family_profile_id")
    private Long familyProfileId;

    @Column(name = "plan_id")
    private Long planId;

    @Column(name = "plan_type")
    @Enumerated(EnumType.STRING)
    private PlanType planType;

    @Column(name = "allocated_amount", precision = 15, scale = 2)
    private BigDecimal allocatedAmount;

    @Column(name = "total_monthly_savings", precision = 15, scale = 2)
    private BigDecimal totalMonthlySavings;

    @Column(name = "allocation_date")
    private LocalDateTime allocationDate;

    @Column(name = "allocation_strategy")
    private String allocationStrategy;

    // Constructors, getters, setters
}

