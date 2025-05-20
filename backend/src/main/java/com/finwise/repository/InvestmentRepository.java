package com.finwise.repository;

import com.finwise.entity.Investment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestmentRepository extends JpaRepository<Investment,Long> {
}
