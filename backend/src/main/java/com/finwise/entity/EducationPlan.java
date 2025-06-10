package com.finwise.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private BigDecimal inflationRate;
    private String notes;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime lastUpdatedDate;

    @ManyToOne
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    public void setInflationAdjustedCost(BigDecimal bigDecimal) {
        this.inflationRate = bigDecimal;
    }

    public void setFamilyProfile(FamilyProfile familyProfile) {
        this.child.setFamilyProfile(familyProfile);
    }

    public FamilyProfile getFamilyProfile() {
        return this.child.getFamilyProfile();
    }
}
