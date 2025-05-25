package com.finwise.repository;

import com.finwise.entity.Child;
import com.finwise.entity.FamilyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ChildRepository extends JpaRepository<Child,Long> {
    Collection<Object> findByFamilyProfile(FamilyProfile familyProfile);
}
