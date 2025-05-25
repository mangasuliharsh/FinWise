package com.finwise.repository;

import com.finwise.entity.MarriageCostReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface MarriageCostReferenceRepository extends JpaRepository<MarriageCostReference,Long> {
    Collection<Object> findByRegion(String region);
}
