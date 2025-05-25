package com.finwise.controller;

import com.finwise.dto.ReportDTO;
import com.finwise.exception.ResourceNotFoundException;
import com.finwise.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService service) {
        this.reportService = service;
    }

    @PostMapping("/{familyProfileId}")
    public ResponseEntity<ReportDTO> createReport(
            @PathVariable Long familyProfileId,
            @RequestBody ReportDTO dto) throws ResourceNotFoundException {
        return ResponseEntity.ok(reportService.createReport(familyProfileId, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> getReport(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(reportService.getReport(id));
    }

    @GetMapping("/family/{familyProfileId}")
    public ResponseEntity<List<ReportDTO>> getAllReports(@PathVariable Long familyProfileId) throws ResourceNotFoundException {
        return ResponseEntity.ok(reportService.getAllReportsByFamily(familyProfileId));
    }

    @GetMapping("/family/{familyProfileId}/type/{reportType}")
    public ResponseEntity<List<ReportDTO>> getReportsByType(
            @PathVariable Long familyProfileId,
            @PathVariable String reportType) throws ResourceNotFoundException {
        return ResponseEntity.ok(reportService.getReportsByType(familyProfileId, reportType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportDTO> updateReport(@PathVariable Long id, @RequestBody ReportDTO dto) throws ResourceNotFoundException {
        return ResponseEntity.ok(reportService.updateReport(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) throws ResourceNotFoundException {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReportDTO> patchReport(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) throws ResourceNotFoundException {
        ReportDTO updated = reportService.patchReport(id, updates);
        return ResponseEntity.ok(updated);
    }
}
