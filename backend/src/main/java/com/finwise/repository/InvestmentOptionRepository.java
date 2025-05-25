package com.finwise.repository;

import com.finwise.entity.InvestmentOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface InvestmentOptionRepository extends JpaRepository<InvestmentOption,Long> {
    Collection<Object> findByRiskLevel(String riskLevel);
}
