package com.finwise.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "economic_indicators")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EconomicIndicator {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String indicatorName;
    private BigDecimal value;
    private int year;
    private Integer month;
    private String dataSource;
}
