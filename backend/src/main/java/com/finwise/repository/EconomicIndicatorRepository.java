package com.finwise.repository;

import com.finwise.entity.EconomicIndicator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EconomicIndicatorRepository extends JpaRepository<EconomicIndicator,Long> {
}
