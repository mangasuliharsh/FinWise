package com.finwise.repository;

import com.finwise.entity.FamilyProfile;
import com.finwise.entity.MarriagePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface MarriagePlanRepository extends JpaRepository<MarriagePlan,Long> {
    List<MarriagePlan> findByFamilyProfileId(Long familyProfileId);

    List<MarriagePlan> findByFamilyProfile(FamilyProfile familyProfile);

    List<MarriagePlan> findByFamilyProfile_Id(Long familyProfileId);
}
