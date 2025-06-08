package com.finwise.service;

import com.finwise.dto.InvestmentPlanDTO;
import com.finwise.entity.InvestmentPlan;
import com.finwise.exception.ResourceNotFoundException;
import com.finwise.repository.InvestmentPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InvestmentPlanService {

    private final InvestmentPlanRepository investmentPlanRepository;

    public InvestmentPlanDTO createPlan(Long familyProfileId, InvestmentPlanDTO dto) {
        dto.setFamilyProfileId(familyProfileId);
        InvestmentPlan plan = convertToEntity(dto);
        InvestmentPlan savedPlan = investmentPlanRepository.save(plan);
        return convertToDTO(savedPlan);
    }

    public InvestmentPlanDTO getPlan(Long id) throws ResourceNotFoundException {
        InvestmentPlan plan = investmentPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investment plan not found with id: " + id));
        return convertToDTO(plan);
    }

    public List<InvestmentPlanDTO> getAllPlansByFamily(Long familyProfileId) {
        List<InvestmentPlan> plans = investmentPlanRepository.findByFamilyProfileId(familyProfileId);
        return plans.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public InvestmentPlanDTO updatePlan(Long id, InvestmentPlanDTO dto) throws ResourceNotFoundException {
        InvestmentPlan existingPlan = investmentPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investment plan not found with id: " + id));

        existingPlan.setPlanName(dto.getPlanName());
        existingPlan.setGoalAmount(dto.getGoalAmount());
        existingPlan.setCurrentSavings(dto.getCurrentSavings());
        existingPlan.setMonthlyContribution(dto.getMonthlyContribution());
        existingPlan.setExpectedReturn(dto.getExpectedReturn());
        existingPlan.setTargetYear(dto.getTargetYear());
        existingPlan.setNotes(dto.getNotes());

        InvestmentPlan savedPlan = investmentPlanRepository.save(existingPlan);
        return convertToDTO(savedPlan);
    }

    public void deletePlan(Long id) throws ResourceNotFoundException {
        if (!investmentPlanRepository.existsById(id)) {
            throw new ResourceNotFoundException("Investment plan not found with id: " + id);
        }
        investmentPlanRepository.deleteById(id);
    }

    private InvestmentPlanDTO convertToDTO(InvestmentPlan plan) {
        return new InvestmentPlanDTO(
                plan.getId(),
                plan.getPlanName(),
                plan.getGoalAmount(),
                plan.getCurrentSavings(),
                plan.getMonthlyContribution(),
                plan.getExpectedReturn(),
                plan.getTargetYear(),
                plan.getNotes(),
                plan.getFamilyProfileId()
        );
    }

    private InvestmentPlan convertToEntity(InvestmentPlanDTO dto) {
        InvestmentPlan plan = new InvestmentPlan();
        plan.setId(dto.getId());
        plan.setPlanName(dto.getPlanName());
        plan.setGoalAmount(dto.getGoalAmount());
        plan.setCurrentSavings(dto.getCurrentSavings());
        plan.setMonthlyContribution(dto.getMonthlyContribution());
        plan.setExpectedReturn(dto.getExpectedReturn());
        plan.setTargetYear(dto.getTargetYear());
        plan.setNotes(dto.getNotes());
        plan.setFamilyProfileId(dto.getFamilyProfileId());
        return plan;
    }
}
