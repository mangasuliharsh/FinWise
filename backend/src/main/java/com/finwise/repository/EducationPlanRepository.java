package com.finwise.repository;

import com.finwise.entity.EducationPlan;
import com.finwise.entity.Child;
import com.finwise.entity.FamilyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EducationPlanRepository extends JpaRepository<EducationPlan, Long> {

    List<EducationPlan> findByChild(Child child);

    List<EducationPlan> findByChild_FamilyProfile(FamilyProfile familyProfile);

    @Modifying
    @Transactional
    @Query("DELETE FROM EducationPlan e WHERE e.child = :child")
    void deleteByChild(@Param("child") Child child);

    List<EducationPlan> findByChild_FamilyProfile_Id(Long familyProfileId);
}
