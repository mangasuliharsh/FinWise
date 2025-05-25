package com.finwise.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "family_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FamilyProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int familySize;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpenses;
    private String location;

    @Column(name = "risk_tolerance")
    private String riskTolerance = "MEDIUM";

    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime lastUpdatedDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "familyProfile", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Child> children;

    @OneToMany(mappedBy = "familyProfile", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MarriagePlan> marriagePlans;

    @OneToMany(mappedBy = "familyProfile", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SavingsPlan> savingsPlans;
}

