package com.finwise.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "investments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Investment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private LocalDate startDate;
    private LocalDate maturityDate;
    private BigDecimal currentValue;
    private String status = "ACTIVE";

    @ManyToOne
    @JoinColumn(name = "savings_plan_id", nullable = false)
    private SavingsPlan savingsPlan;

    @ManyToOne
    @JoinColumn(name = "investment_option_id", nullable = false)
    private InvestmentOption investmentOption;
}

