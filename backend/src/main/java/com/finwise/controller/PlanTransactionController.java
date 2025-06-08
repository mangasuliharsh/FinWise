package com.finwise.controller;

import com.finwise.dto.PlanTransactionDTO;
import com.finwise.entity.PlanTransaction;
import com.finwise.service.PlanTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plan-transactions")
@RequiredArgsConstructor
public class PlanTransactionController {
    private final PlanTransactionService service;

    @PostMapping
    public ResponseEntity<PlanTransaction> addTransaction(@RequestBody PlanTransactionDTO dto) {
        PlanTransaction tx = service.addTransaction(dto.getFamilyProfileId(), dto.getPlanType(), dto.getPlanId(), dto.getAction(), dto.getAmount(), dto.getDescription());
        return ResponseEntity.ok(tx);
    }

    @GetMapping("/recent/{familyProfileId}")
    public ResponseEntity<List<PlanTransaction>> getRecent(@PathVariable Long familyProfileId) {
        return ResponseEntity.ok(service.getRecentTransactions(familyProfileId));
    }
}
