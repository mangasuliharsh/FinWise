package com.finwise.controller;

import com.finwise.dto.EconomicIndicatorDTO;
import com.finwise.exception.ResourceNotFoundException;
import com.finwise.service.EconomicIndicatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/economic-indicators")
public class EconomicIndicatorController {

    private final EconomicIndicatorService economicIndicatorService;

    public EconomicIndicatorController(EconomicIndicatorService service) {
        this.economicIndicatorService = service;
    }

    @PostMapping
    public ResponseEntity<EconomicIndicatorDTO> createEconomicIndicator(@RequestBody EconomicIndicatorDTO dto) {
        return ResponseEntity.ok(economicIndicatorService.createEconomicIndicator(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EconomicIndicatorDTO> getEconomicIndicator(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(economicIndicatorService.getEconomicIndicator(id));
    }

    @GetMapping
    public ResponseEntity<List<EconomicIndicatorDTO>> getAllEconomicIndicators() {
        return ResponseEntity.ok(economicIndicatorService.getAllEconomicIndicators());
    }


    @PutMapping("/{id}")
    public ResponseEntity<EconomicIndicatorDTO> updateEconomicIndicator(
            @PathVariable Long id,
            @RequestBody EconomicIndicatorDTO dto) throws ResourceNotFoundException {
        return ResponseEntity.ok(economicIndicatorService.updateEconomicIndicator(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEconomicIndicator(@PathVariable Long id) throws ResourceNotFoundException {
        economicIndicatorService.deleteEconomicIndicator(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EconomicIndicatorDTO> patchEconomicIndicator(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) throws ResourceNotFoundException {
        EconomicIndicatorDTO updated = economicIndicatorService.patchEconomicIndicator(id, updates);
        return ResponseEntity.ok(updated);
    }
}
