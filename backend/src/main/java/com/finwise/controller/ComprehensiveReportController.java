package com.finwise.controller;

import com.finwise.dto.ComprehensiveReportDTO;
import com.finwise.dto.EducationPlanProgressDTO;
import com.finwise.dto.InvestmentPlanProgressDTO;
import com.finwise.dto.MarriagePlanProgressDTO;
import com.finwise.service.ComprehensiveReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ComprehensiveReportController {

    private final ComprehensiveReportService reportService;

    @GetMapping("/comprehensive/{userId}")
    public ResponseEntity<?> getComprehensiveReport(@PathVariable Long userId) {
        try {
            ComprehensiveReportDTO report = reportService.generateComprehensiveReport(userId);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating report: " + e.getMessage());
        }
    }

    @GetMapping("/marriage/{userId}")
    public ResponseEntity<?> getMarriagePlansReport(@PathVariable Long userId) {
        try {
            List<MarriagePlanProgressDTO> marriagePlans = reportService.calculateMarriagePlansProgress(userId);
            return ResponseEntity.ok(marriagePlans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating marriage plans report: " + e.getMessage());
        }
    }

    @GetMapping("/education/{userId}")
    public ResponseEntity<?> getEducationPlansReport(@PathVariable Long userId) {
        try {
            List<EducationPlanProgressDTO> educationPlans = reportService.calculateEducationPlansProgress(userId);
            return ResponseEntity.ok(educationPlans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating education plans report: " + e.getMessage());
        }
    }

    @GetMapping("/investments/{userId}")
    public ResponseEntity<?> getInvestmentPlansReport(@PathVariable Long userId) {
        try {
            List<InvestmentPlanProgressDTO> investmentPlans = reportService.calculateInvestmentPlansProgress(userId);
            return ResponseEntity.ok(investmentPlans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating investment plans report: " + e.getMessage());
        }
    }
}
