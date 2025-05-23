package com.finwise.controller;

import com.finwise.dto.EducationFundingGapDTO;
import com.finwise.entity.EducationPlan;
import com.finwise.service.EducationPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/education-plans")
@CrossOrigin(origins = "*")
public class EducationPlanController {

    @Autowired
    private EducationPlanService educationPlanService;

    @PostMapping
    public EducationPlan createEducationPlan(@RequestBody EducationPlan plan) {
        return educationPlanService.createEducationPlan(plan);
    }

    @GetMapping("/child/{childId}")
    public List<EducationPlan> getEducationPlansByChildId(@PathVariable Long childId) {
        return educationPlanService.getEducationPlansByChildId(childId);
    }

    @GetMapping("/family/{familyProfileId}")
    public List<EducationPlan> getEducationPlansByFamilyId(@PathVariable Long familyProfileId) {
        return educationPlanService.getEducationPlansByFamilyId(familyProfileId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EducationPlan> getEducationPlanById(@PathVariable Long id) {
        return educationPlanService.getEducationPlanById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EducationPlan> updateEducationPlan(
            @PathVariable Long id,
            @RequestBody EducationPlan plan) {
        return ResponseEntity.ok(educationPlanService.updateEducationPlan(id, plan));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducationPlan(@PathVariable Long id) {
        educationPlanService.deleteEducationPlan(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/funding-gap")
    public ResponseEntity<EducationFundingGapDTO> calculateFundingGap(@PathVariable Long id) {
        return ResponseEntity.ok(educationPlanService.calculateFundingGap(id));
    }
}
