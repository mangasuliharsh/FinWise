package com.finwise.repository;

import com.finwise.entity.Child;
import com.finwise.entity.EducationPlan;
import com.finwise.entity.FamilyProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface EducationPlanRepository extends JpaRepository<EducationPlan,Long> {
    Collection<Object> findByChild(Child child);

    List<EducationPlan> findByChild_FamilyProfile(FamilyProfile childFamilyProfile);

    List<EducationPlan> findByChild_FamilyProfile_Id(Long familyProfileId);
}
