package com.finwise.repository;

import com.finwise.entity.SavingsPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingsPlanRepository extends JpaRepository<SavingsPlan,Long> {
}
