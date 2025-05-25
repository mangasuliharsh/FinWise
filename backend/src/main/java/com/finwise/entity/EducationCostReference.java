package com.finwise.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "education_cost_references")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationCostReference {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String educationLevel;
    private String institutionType;
    private String region;
    private BigDecimal averageAnnualCost;
    private int year;
    private String dataSource;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime lastUpdatedDate;
}

