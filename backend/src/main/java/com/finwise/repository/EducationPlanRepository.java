package com.finwise.repository;

import com.finwise.entity.EducationPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EducationPlanRepository extends JpaRepository<EducationPlan,Long> {
    List<EducationPlan> findByChildId(Long childId);
}
