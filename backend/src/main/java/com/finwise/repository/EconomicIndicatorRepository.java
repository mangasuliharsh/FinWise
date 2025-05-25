package com.finwise.repository;

import com.finwise.entity.EconomicIndicator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;

public interface EconomicIndicatorRepository extends JpaRepository<EconomicIndicator,Long> {
}
