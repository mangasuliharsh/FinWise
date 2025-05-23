package com.finwise.controller;

import com.finwise.entity.EducationCostReference;
import com.finwise.repository.EducationCostReferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/education-costs")
@CrossOrigin(origins = "*")
public class EducationCostReferenceController {

    @Autowired
    private EducationCostReferenceRepository costReferenceRepository;

    @GetMapping
    public List<EducationCostReference> getAllCostReferences() {
        return costReferenceRepository.findAll();
    }

    @GetMapping("/latest")
    public ResponseEntity<EducationCostReference> getLatestCostReference(
            @RequestParam String educationLevel,
            @RequestParam String institutionType) {
        return costReferenceRepository
                .findTopByEducationLevelAndInstitutionTypeOrderByYearDesc(educationLevel, institutionType)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public EducationCostReference createCostReference(@RequestBody EducationCostReference costReference) {
        return costReferenceRepository.save(costReference);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCostReference(@PathVariable Long id) {
        if (!costReferenceRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        costReferenceRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
