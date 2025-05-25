package com.finwise.repository;

import com.finwise.entity.EducationCostReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface EducationCostReferenceRepository extends JpaRepository<EducationCostReference,Long> {
    Collection<Object> findByRegion(String region);
}
