package com.finwise.repository;

import com.finwise.entity.Report;
import com.finwise.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ReportRepository extends JpaRepository<Report,Long> {
    Collection<Object> findByUser(User user);

    Collection<Object> findByUserAndReportType(User user, String reportType);
}
