package com.finwise.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "children")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Child {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate dateOfBirth;
    private String currentEducationLevel;

    @ManyToOne
    @JoinColumn(name = "family_profile_id", nullable = false)
    private FamilyProfile familyProfile;

    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL)
    private List<EducationPlan> educationPlans;
}
