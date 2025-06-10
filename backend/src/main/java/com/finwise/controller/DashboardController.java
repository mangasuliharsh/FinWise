package com.finwise.controller;

import com.finwise.dto.DashboardSummaryDTO;
import com.finwise.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary/{familyProfileId}")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary(@PathVariable Long familyProfileId) {
        log.info("Fetching dashboard summary for family profile ID: {}", familyProfileId);
        DashboardSummaryDTO summary = dashboardService.getDashboardSummary(familyProfileId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/refresh/{familyProfileId}")
    public ResponseEntity<DashboardSummaryDTO> refreshDashboard(@PathVariable Long familyProfileId) {
        log.info("Refreshing dashboard data for family profile ID: {}", familyProfileId);
        DashboardSummaryDTO summary = dashboardService.refreshDashboardData(familyProfileId);
        return ResponseEntity.ok(summary);
    }
}
