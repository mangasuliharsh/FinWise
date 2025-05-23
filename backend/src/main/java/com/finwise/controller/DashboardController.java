package com.finwise.controller;

import com.finwise.dto.DashboardDataDTO;
import com.finwise.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/family/{familyProfileId}")
    public ResponseEntity<DashboardDataDTO> getDashboardData(@PathVariable Long familyProfileId) {
        return ResponseEntity.ok(dashboardService.getDashboardData(familyProfileId));
    }
}
