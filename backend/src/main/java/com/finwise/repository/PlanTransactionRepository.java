package com.finwise.repository;

import com.finwise.entity.PlanTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PlanTransactionRepository extends JpaRepository<PlanTransaction, Long> {
    List<PlanTransaction> findTop5ByFamilyProfile_IdOrderByDateTimeDesc(Long familyProfileId);
}
