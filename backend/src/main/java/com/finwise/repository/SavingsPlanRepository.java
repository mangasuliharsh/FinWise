package com.finwise.repository;

import com.finwise.entity.FamilyProfile;
import com.finwise.entity.SavingsPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface SavingsPlanRepository extends JpaRepository<SavingsPlan,Long> {
    Collection<Object> findByFamilyProfile(FamilyProfile familyProfile);
}
