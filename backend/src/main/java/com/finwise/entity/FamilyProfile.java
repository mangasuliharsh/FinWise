package com.finwise.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "family_profile")
public class FamilyProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "primary_guardian_name", nullable = false)
    private String primaryGuardianName;

    @Column(name = "primary_guardian_email", nullable = false)
    private String primaryGuardianEmail;

    @Column(name = "primary_guardian_phone")
    private String primaryGuardianPhone;

    @Column(name = "secondary_guardian_name")
    private String secondaryGuardianName;

    @Column(name = "secondary_guardian_email")
    private String secondaryGuardianEmail;

    @Column(name = "secondary_guardian_phone")
    private String secondaryGuardianPhone;

    @Column(nullable = false)
    private String address;

    @Column(name = "annual_income", nullable = false)
    private BigDecimal annualIncome;

    @Column(nullable = false)
    private boolean onboardingComplete = false;

    @OneToMany(mappedBy = "familyProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Child> children = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addChild(Child child) {
        children.add(child);
        child.setFamilyProfile(this);
    }

    public void removeChild(Child child) {
        children.remove(child);
        child.setFamilyProfile(null);
    }
}
