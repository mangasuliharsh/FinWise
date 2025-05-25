package com.finwise.controller;

import com.finwise.dto.InvestmentOptionDTO;
import com.finwise.exception.ResourceNotFoundException;
import com.finwise.service.InvestmentOptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/investment-options")
public class InvestmentOptionController {

    private final InvestmentOptionService investmentOptionService;

    public InvestmentOptionController(InvestmentOptionService service) {
        this.investmentOptionService = service;
    }

    @PostMapping
    public ResponseEntity<InvestmentOptionDTO> createInvestmentOption(@RequestBody InvestmentOptionDTO dto) {
        return ResponseEntity.ok(investmentOptionService.createInvestmentOption(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvestmentOptionDTO> getInvestmentOption(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(investmentOptionService.getInvestmentOption(id));
    }

    @GetMapping
    public ResponseEntity<List<InvestmentOptionDTO>> getAllInvestmentOptions() {
        return ResponseEntity.ok(investmentOptionService.getAllInvestmentOptions());
    }

    @GetMapping("/risk-level/{riskLevel}")
    public ResponseEntity<List<InvestmentOptionDTO>> getInvestmentOptionsByRiskLevel(@PathVariable String riskLevel) {
        return ResponseEntity.ok(investmentOptionService.getInvestmentOptionsByRiskLevel(riskLevel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvestmentOptionDTO> updateInvestmentOption(
            @PathVariable Long id,
            @RequestBody InvestmentOptionDTO dto) throws ResourceNotFoundException {
        return ResponseEntity.ok(investmentOptionService.updateInvestmentOption(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvestmentOption(@PathVariable Long id) throws ResourceNotFoundException {
        investmentOptionService.deleteInvestmentOption(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<InvestmentOptionDTO> patchInvestmentOption(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) throws ResourceNotFoundException {
        InvestmentOptionDTO updated = investmentOptionService.patchInvestmentOption(id, updates);
        return ResponseEntity.ok(updated);
    }
}
