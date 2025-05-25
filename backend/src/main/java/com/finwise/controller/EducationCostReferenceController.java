package com.finwise.controller;

import com.finwise.dto.EducationCostReferenceDTO;
import com.finwise.exception.ResourceNotFoundException;
import com.finwise.service.EducationCostReferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/education-cost-references")
public class EducationCostReferenceController {

    private final EducationCostReferenceService educationCostReferenceService;

    public EducationCostReferenceController(EducationCostReferenceService service) {
        this.educationCostReferenceService = service;
    }

    @PostMapping
    public ResponseEntity<EducationCostReferenceDTO> createEducationCostReference(@RequestBody EducationCostReferenceDTO dto) {
        return ResponseEntity.ok(educationCostReferenceService.createEducationCostReference(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EducationCostReferenceDTO> getEducationCostReference(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(educationCostReferenceService.getEducationCostReference(id));
    }

    @GetMapping
    public ResponseEntity<List<EducationCostReferenceDTO>> getAllEducationCostReferences() {
        return ResponseEntity.ok(educationCostReferenceService.getAllEducationCostReferences());
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<EducationCostReferenceDTO>> getEducationCostReferencesByLocation(@PathVariable String location) {
        return ResponseEntity.ok(educationCostReferenceService.getEducationCostReferencesByLocation(location));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EducationCostReferenceDTO> updateEducationCostReference(
            @PathVariable Long id,
            @RequestBody EducationCostReferenceDTO dto) throws ResourceNotFoundException {
        return ResponseEntity.ok(educationCostReferenceService.updateEducationCostReference(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducationCostReference(@PathVariable Long id) throws ResourceNotFoundException {
        educationCostReferenceService.deleteEducationCostReference(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EducationCostReferenceDTO> patchEducationCostReference(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) throws ResourceNotFoundException {
        EducationCostReferenceDTO updated = educationCostReferenceService.patchEducationCostReference(id, updates);
        return ResponseEntity.ok(updated);
    }
}
