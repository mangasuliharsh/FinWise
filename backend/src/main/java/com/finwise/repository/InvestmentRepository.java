package com.finwise.repository;

import com.finwise.entity.FamilyProfile;
import com.finwise.entity.Investment;
import com.finwise.entity.SavingsPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface InvestmentRepository extends JpaRepository<Investment,Long> {
    Collection<Object> findBySavingsPlan(SavingsPlan savingsPlan);
    Collection<Object> findBySavingsPlan_FamilyProfile_Id(Long familyProfileId);
}
