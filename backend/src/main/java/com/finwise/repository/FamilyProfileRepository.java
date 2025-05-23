package com.finwise.repository;

import com.finwise.entity.FamilyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyProfileRepository extends JpaRepository<FamilyProfile, Long> {
}
