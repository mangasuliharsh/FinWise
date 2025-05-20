package com.finwise.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "marriage_cost_references")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarriageCostReference {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String region;
    private BigDecimal averageCost;
    private int year;
    private String dataSource;
}

