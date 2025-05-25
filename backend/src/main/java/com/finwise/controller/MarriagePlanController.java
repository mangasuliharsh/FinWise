
package com.finwise.controller;

import com.finwise.dto.MarriagePlanDTO;
import com.finwise.service.MarriagePlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/marriage-plans")
public class MarriagePlanController {

    private final MarriagePlanService marriagePlanServiceservice;

    public MarriagePlanController(MarriagePlanService service) {
        this.marriagePlanServiceservice = service;
    }

    @PostMapping("/{familyProfileId}")
    public ResponseEntity<MarriagePlanDTO> createMarriagePlan(
            @PathVariable Long familyProfileId,
            @RequestBody MarriagePlanDTO dto) {
        return ResponseEntity.ok(marriagePlanServiceservice.createMarriagePlan(familyProfileId, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarriagePlanDTO> getPlan(@PathVariable Long id) {
        return ResponseEntity.ok(marriagePlanServiceservice.getMarriagePlan(id));
    }

    @GetMapping("/family/{familyProfileId}")
    public ResponseEntity<List<MarriagePlanDTO>> getAllPlans(@PathVariable Long familyProfileId) {
        return ResponseEntity.ok(marriagePlanServiceservice.getAllPlansByFamily(familyProfileId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarriagePlanDTO> updatePlan(@PathVariable Long id, @RequestBody MarriagePlanDTO dto) {
        return ResponseEntity.ok(marriagePlanServiceservice.updateMarriagePlan(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        marriagePlanServiceservice.deleteMarriagePlan(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<MarriagePlanDTO> patchMarriagePlan(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        MarriagePlanDTO updated = marriagePlanServiceservice.patchMarriagePlan(id, updates);
        return ResponseEntity.ok(updated);
    }
}


