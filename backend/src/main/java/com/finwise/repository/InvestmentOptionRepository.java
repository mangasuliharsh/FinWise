package com.finwise.repository;

import com.finwise.entity.InvestmentOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestmentOptionRepository extends JpaRepository<InvestmentOption,Long> {
}
