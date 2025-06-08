package com.finwise.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "savings_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavingsPlan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String planName;
    private BigDecimal goalAmount;
    private BigDecimal currentAmount = BigDecimal.ZERO;
    private BigDecimal monthlyContribution = BigDecimal.ZERO;
    private LocalDate targetCompletionDate;
    private String purpose;
    private String priority = "MEDIUM";
    private String notes;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime lastUpdatedDate;

    @ManyToOne
    @JoinColumn(name = "family_profile_id", nullable = false)
    private FamilyProfile familyProfile;


}
