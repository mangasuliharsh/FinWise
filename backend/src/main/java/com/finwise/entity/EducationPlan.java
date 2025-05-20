package com.finwise.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "education_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationPlan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String planName;
    private String educationLevel;
    private String institutionType;
    private int estimatedStartYear;
    private int estimatedEndYear;
    private BigDecimal estimatedTotalCost;
    private BigDecimal currentSavings = BigDecimal.ZERO;
    private BigDecimal monthlyContribution = BigDecimal.ZERO;
    private BigDecimal inflationRate = new BigDecimal("6.00");
    private String notes;

    @ManyToOne
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;
}
