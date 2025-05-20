package com.finwise.repository;

import com.finwise.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildRepository extends JpaRepository<Child,Long> {
}
