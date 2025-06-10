package com.finwise.service;

import com.finwise.entity.FamilyProfile;
import com.finwise.entity.PlanTransaction;
import com.finwise.repository.FamilyProfileRepository;
import com.finwise.repository.PlanTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanTransactionService {
    private final PlanTransactionRepository repo;
    private final FamilyProfileRepository familyRepo;

    public PlanTransaction addTransaction(Long familyProfileId, String planType, Long planId, String action, BigDecimal amount, String description) {
        Optional<FamilyProfile> family = familyRepo.findById(familyProfileId);
        PlanTransaction tx = new PlanTransaction();
        tx.setFamilyProfile(family.get());
        tx.setPlanType(planType);
        tx.setPlanId(planId);
        tx.setAction(action);
        tx.setAmount(amount);
        tx.setDescription(description);
        tx.setDateTime(LocalDateTime.now());
        return repo.save(tx);
    }

    public List<PlanTransaction> getRecentTransactions(FamilyProfile familyProfile) {
        return repo.findTop5ByFamilyProfileOrderByDateTimeDesc(familyProfile);
    }
}
