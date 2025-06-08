package com.finwise.entity;

import com.finwise.entity.FamilyProfile;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class PlanTransaction {
    @Id
    @GeneratedValue
    private Long id;

    private String planType; // "child", "education", "investment"
    private Long planId;     // ID of the plan (EducationPlan, Investment, etc.)
    private String action;   // "add" or "remove"
    private BigDecimal amount;
    private String description;
    private LocalDateTime dateTime;

    @ManyToOne
    private FamilyProfile familyProfile;

    public void setFamilyProfile(FamilyProfile family) {
    }

    public void setPlanType(String planType) {
    }
}
