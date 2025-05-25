package com.finwise.controller;

import com.finwise.dto.SavingsPlanDTO;
import com.finwise.exception.ResourceNotFoundException;
import com.finwise.service.SavingsPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/savings-plans")
public class SavingsPlanController {

    private final SavingsPlanService savingsPlanService;

    public SavingsPlanController(SavingsPlanService service) {
        this.savingsPlanService = service;
    }

    @PostMapping("/{familyProfileId}")
    public ResponseEntity<SavingsPlanDTO> createSavingsPlan(
            @PathVariable Long familyProfileId,
            @RequestBody SavingsPlanDTO dto) throws ResourceNotFoundException {
        return ResponseEntity.ok(savingsPlanService.createSavingsPlan(familyProfileId, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SavingsPlanDTO> getPlan(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(savingsPlanService.getSavingsPlan(id));
    }

    @GetMapping("/family/{familyProfileId}")
    public ResponseEntity<List<SavingsPlanDTO>> getAllPlans(@PathVariable Long familyProfileId) throws ResourceNotFoundException {
        return ResponseEntity.ok(savingsPlanService.getAllPlansByFamily(familyProfileId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SavingsPlanDTO> updatePlan(@PathVariable Long id, @RequestBody SavingsPlanDTO dto) throws ResourceNotFoundException {
        return ResponseEntity.ok(savingsPlanService.updateSavingsPlan(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) throws ResourceNotFoundException {
        savingsPlanService.deleteSavingsPlan(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SavingsPlanDTO> patchSavingsPlan(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) throws ResourceNotFoundException {
        SavingsPlanDTO updated = savingsPlanService.patchSavingsPlan(id, updates);
        return ResponseEntity.ok(updated);
    }
}
