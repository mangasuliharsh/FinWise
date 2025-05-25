package com.finwise.controller;

import com.finwise.dto.InvestmentDTO;
import com.finwise.exception.ResourceNotFoundException;
import com.finwise.service.InvestmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/investments")
public class InvestmentController {

    private final InvestmentService investmentService;

    public InvestmentController(InvestmentService service) {
        this.investmentService = service;
    }

    @PostMapping("/{familyProfileId}")
    public ResponseEntity<InvestmentDTO> createInvestment(
            @PathVariable Long familyProfileId,
            @RequestBody InvestmentDTO dto) throws ResourceNotFoundException {
        return ResponseEntity.ok(investmentService.createInvestment(familyProfileId, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvestmentDTO> getInvestment(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(investmentService.getInvestment(id));
    }

    @GetMapping("/family/{familyProfileId}")
    public ResponseEntity<List<InvestmentDTO>> getAllInvestments(@PathVariable Long familyProfileId) {
        return ResponseEntity.ok(investmentService.getAllInvestmentsByFamily(familyProfileId));
    }

    @GetMapping("/savings-plan/{savingsPlanId}")
    public ResponseEntity<List<InvestmentDTO>> getInvestmentsBySavingsPlan(@PathVariable Long savingsPlanId) throws ResourceNotFoundException {
        return ResponseEntity.ok(investmentService.getInvestmentsBySavingsPlan(savingsPlanId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvestmentDTO> updateInvestment(@PathVariable Long id, @RequestBody InvestmentDTO dto) throws ResourceNotFoundException {
        return ResponseEntity.ok(investmentService.updateInvestment(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvestment(@PathVariable Long id) throws ResourceNotFoundException {
        investmentService.deleteInvestment(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<InvestmentDTO> patchInvestment(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) throws ResourceNotFoundException {
        InvestmentDTO updated = investmentService.patchInvestment(id, updates);
        return ResponseEntity.ok(updated);
    }
}
