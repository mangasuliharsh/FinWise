package com.finwise.repository;

import com.finwise.entity.MarriageExpenseCategory;
import com.finwise.entity.MarriagePlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface MarriageExpenseCategoryRepository extends JpaRepository<MarriageExpenseCategory,Long> {
    Collection<Object> findByMarriagePlan(MarriagePlan marriagePlan);
}
