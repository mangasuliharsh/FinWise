package com.finwise.repository;

import com.finwise.entity.EducationCostReference;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface EducationCostReferenceRepository extends JpaRepository<EducationCostReference,Long> {
    Optional<EducationCostReference> findTopByEducationLevelAndInstitutionTypeOrderByYearDesc(String educationLevel, String institutionType);
    List<EducationCostReference> findByEducationLevelAndInstitutionType(String educationLevel, String institutionType);
}
