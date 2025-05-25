package com.finwise.controller;

import com.finwise.dto.EducationPlanDTO;
import com.finwise.service.EducationPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/education-plans")
public class EducationPlanController {

    private final EducationPlanService educationPlanService;

    public EducationPlanController(EducationPlanService service) {
        this.educationPlanService = service;
    }

    @PostMapping("/{familyProfileId}")
    public ResponseEntity<EducationPlanDTO> createEducationPlan(
            @PathVariable Long familyProfileId,
            @RequestBody EducationPlanDTO dto) {
        return ResponseEntity.ok(educationPlanService.createEducationPlan(familyProfileId, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EducationPlanDTO> getPlan(@PathVariable Long id) {
        return ResponseEntity.ok(educationPlanService.getEducationPlan(id));
    }


    @GetMapping("/child/{childId}")
    public ResponseEntity<List<EducationPlanDTO>> getPlansByChild(@PathVariable Long childId) {
        return ResponseEntity.ok(educationPlanService.getPlansByChild(childId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EducationPlanDTO> updatePlan(@PathVariable Long id, @RequestBody EducationPlanDTO dto) {
        return ResponseEntity.ok(educationPlanService.updateEducationPlan(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        educationPlanService.deleteEducationPlan(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EducationPlanDTO> patchEducationPlan(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        EducationPlanDTO updated = educationPlanService.patchEducationPlan(id, updates);
        return ResponseEntity.ok(updated);
    }
}
