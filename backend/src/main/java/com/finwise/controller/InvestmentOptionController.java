package com.finwise.controller;


import com.finwise.service.InvestmentOptionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/investment-options")
public class InvestmentOptionController {

    private final InvestmentOptionsService investmentOptionsService;

    public InvestmentOptionController(InvestmentOptionsService service) {
        this.investmentOptionsService = service;
    }

    @GetMapping("/{familyProfileId}")
    public ResponseEntity<Map<String, Object>> getInvestmentOptions(@PathVariable Long familyProfileId) {
        try {
            Map<String, Object> response = investmentOptionsService.getInvestmentOptionsForFamily(familyProfileId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to fetch investment options");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

}
