package com.finwise.controller;

import com.finwise.dto.MarriageExpenseCategoryDTO;
import com.finwise.exception.ResourceNotFoundException;
import com.finwise.service.MarriageExpenseCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/marriage-expense-categories")
public class MarriageExpenseCategoryController {

    private final MarriageExpenseCategoryService marriageExpenseCategoryService;

    public MarriageExpenseCategoryController(MarriageExpenseCategoryService service) {
        this.marriageExpenseCategoryService = service;
    }

    @PostMapping("/{marriagePlanId}")
    public ResponseEntity<MarriageExpenseCategoryDTO> createExpenseCategory(
            @PathVariable Long marriagePlanId,
            @RequestBody MarriageExpenseCategoryDTO dto) throws ResourceNotFoundException {
        return ResponseEntity.ok(marriageExpenseCategoryService.createExpenseCategory(marriagePlanId, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarriageExpenseCategoryDTO> getExpenseCategory(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(marriageExpenseCategoryService.getExpenseCategory(id));
    }

    @GetMapping("/marriage-plan/{marriagePlanId}")
    public ResponseEntity<List<MarriageExpenseCategoryDTO>> getAllExpenseCategories(@PathVariable Long marriagePlanId) throws ResourceNotFoundException {
        return ResponseEntity.ok(marriageExpenseCategoryService.getAllExpenseCategoriesByPlan(marriagePlanId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarriageExpenseCategoryDTO> updateExpenseCategory(
            @PathVariable Long id,
            @RequestBody MarriageExpenseCategoryDTO dto) throws ResourceNotFoundException {
        return ResponseEntity.ok(marriageExpenseCategoryService.updateExpenseCategory(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenseCategory(@PathVariable Long id) throws ResourceNotFoundException {
        marriageExpenseCategoryService.deleteExpenseCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MarriageExpenseCategoryDTO> patchExpenseCategory(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) throws ResourceNotFoundException {
        MarriageExpenseCategoryDTO updated = marriageExpenseCategoryService.patchExpenseCategory(id, updates);
        return ResponseEntity.ok(updated);
    }
}
