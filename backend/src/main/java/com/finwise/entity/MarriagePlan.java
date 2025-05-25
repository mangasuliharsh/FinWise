package com.finwise.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "marriage_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarriagePlan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String planName;
    private String forName;
    private String relationship;
    private int estimatedYear;
    private BigDecimal estimatedTotalCost;
    private BigDecimal currentSavings = BigDecimal.ZERO;
    private BigDecimal monthlyContribution = BigDecimal.ZERO;
    private BigDecimal inflationRate = new BigDecimal("6.00");
    private String notes;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime lastUpdatedDate;
    @ManyToOne
    @JoinColumn(name = "family_profile_id", nullable = false)
    private FamilyProfile familyProfile;

    @OneToMany(mappedBy = "marriagePlan", cascade = CascadeType.ALL)
    private List<MarriageExpenseCategory> expenseCategories;
}
