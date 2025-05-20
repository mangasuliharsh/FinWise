package com.finwise.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "investment_options")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentOption {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String riskLevel;
    private BigDecimal expectedAnnualReturn;
    private Integer minInvestmentPeriod;
    private String description;
    private boolean isActive = true;
}

