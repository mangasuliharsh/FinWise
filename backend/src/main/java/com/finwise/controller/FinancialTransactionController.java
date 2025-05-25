package com.finwise.controller;

import com.finwise.dto.FinancialTransactionDTO;
import com.finwise.exception.ResourceNotFoundException;
import com.finwise.service.FinancialTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/financial-transactions")
public class FinancialTransactionController {

    private final FinancialTransactionService financialTransactionService;

    public FinancialTransactionController(FinancialTransactionService service) {
        this.financialTransactionService = service;
    }

    @PostMapping("/{familyProfileId}")
    public ResponseEntity<FinancialTransactionDTO> createTransaction(
            @PathVariable Long familyProfileId,
            @RequestBody FinancialTransactionDTO dto) throws ResourceNotFoundException {
        return ResponseEntity.ok(financialTransactionService.createTransaction(familyProfileId, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinancialTransactionDTO> getTransaction(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(financialTransactionService.getTransaction(id));
    }

    @GetMapping("/family/{familyProfileId}")
    public ResponseEntity<List<FinancialTransactionDTO>> getAllTransactions(@PathVariable Long familyProfileId) throws ResourceNotFoundException {
        return ResponseEntity.ok(financialTransactionService.getAllTransactionsByFamily(familyProfileId));
    }

    @GetMapping("/family/{familyProfileId}/recent")
    public ResponseEntity<List<FinancialTransactionDTO>> getRecentTransactions(
            @PathVariable Long familyProfileId,
            @RequestParam(defaultValue = "10") int limit) throws ResourceNotFoundException {
        return ResponseEntity.ok(financialTransactionService.getRecentTransactions(familyProfileId, limit));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinancialTransactionDTO> updateTransaction(
            @PathVariable Long id,
            @RequestBody FinancialTransactionDTO dto) throws ResourceNotFoundException {
        return ResponseEntity.ok(financialTransactionService.updateTransaction(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) throws ResourceNotFoundException {
        financialTransactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FinancialTransactionDTO> patchTransaction(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) throws ResourceNotFoundException {
        FinancialTransactionDTO updated = financialTransactionService.patchTransaction(id, updates);
        return ResponseEntity.ok(updated);
    }
}
