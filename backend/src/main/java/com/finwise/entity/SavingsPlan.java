package com.finwise.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @ManyToOne
    @JoinColumn(name = "family_profile_id", nullable = false)
    private FamilyProfile familyProfile;

    @OneToMany(mappedBy = "savingsPlan", cascade = CascadeType.ALL)
    private List<Investment> investments;
}
