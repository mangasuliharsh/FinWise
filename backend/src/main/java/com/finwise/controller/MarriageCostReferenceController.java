package com.finwise.controller;
import com.finwise.dto.MarriageCostReferenceDTO;
import com.finwise.exception.ResourceNotFoundException;
import com.finwise.service.MarriageCostReferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/marriage-cost-references")
public class MarriageCostReferenceController {

    private final MarriageCostReferenceService marriageCostReferenceService;

    public MarriageCostReferenceController(MarriageCostReferenceService service) {
        this.marriageCostReferenceService = service;
    }

    @PostMapping
    public ResponseEntity<MarriageCostReferenceDTO> createMarriageCostReference(@RequestBody MarriageCostReferenceDTO dto) {
        return ResponseEntity.ok(marriageCostReferenceService.createMarriageCostReference(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarriageCostReferenceDTO> getMarriageCostReference(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(marriageCostReferenceService.getMarriageCostReference(id));
    }

    @GetMapping
    public ResponseEntity<List<MarriageCostReferenceDTO>> getAllMarriageCostReferences() {
        return ResponseEntity.ok(marriageCostReferenceService.getAllMarriageCostReferences());
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<MarriageCostReferenceDTO>> getMarriageCostReferencesByLocation(@PathVariable String location) {
        return ResponseEntity.ok(marriageCostReferenceService.getMarriageCostReferencesByLocation(location));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarriageCostReferenceDTO> updateMarriageCostReference(
            @PathVariable Long id,
            @RequestBody MarriageCostReferenceDTO dto) throws ResourceNotFoundException {
        return ResponseEntity.ok(marriageCostReferenceService.updateMarriageCostReference(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarriageCostReference(@PathVariable Long id) throws ResourceNotFoundException {
        marriageCostReferenceService.deleteMarriageCostReference(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MarriageCostReferenceDTO> patchMarriageCostReference(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) throws ResourceNotFoundException {
        MarriageCostReferenceDTO updated = marriageCostReferenceService.patchMarriageCostReference(id, updates);
        return ResponseEntity.ok(updated);
    }
}

