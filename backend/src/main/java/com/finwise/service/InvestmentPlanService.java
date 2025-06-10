package com.finwise.service;

import com.finwise.dto.InvestmentPlanDTO;
import com.finwise.entity.FamilyProfile;
import com.finwise.entity.InvestmentPlan;
import com.finwise.entity.PlanTransaction;
import com.finwise.exception.ResourceNotFoundException;
import com.finwise.repository.FamilyProfileRepository;
import com.finwise.repository.InvestmentPlanRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.spec.OAEPParameterSpec;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InvestmentPlanService {

    private final InvestmentPlanRepository investmentPlanRepository;
    private final PlanTransactionService planTransactionService;
    private final FamilyProfileRepository familyProfileRepository;
    private final ModelMapper modelMapper;

    public InvestmentPlanDTO createPlan(Long familyProfileId, InvestmentPlanDTO dto) {
        dto.setFamilyProfileId(familyProfileId);
        InvestmentPlan plan = convertToEntity(dto);
        InvestmentPlan savedPlan = investmentPlanRepository.save(plan);
        planTransactionService.addTransaction(familyProfileId,"Investment",savedPlan.getId(),"Added",plan.getGoalAmount(),"");
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
        planTransactionService.addTransaction(savedPlan.getFamilyProfileId(), "Investment",savedPlan.getId(),"Updated",savedPlan.getGoalAmount(),"");
        return convertToDTO(savedPlan);
    }

    public void deletePlan(Long id) throws ResourceNotFoundException {
        if (!investmentPlanRepository.existsById(id)) {
            throw new ResourceNotFoundException("Investment plan not found with id: " + id);
        }
        Optional<InvestmentPlan> optionalInvestmentPlan = investmentPlanRepository.findById(id);
        InvestmentPlan savedPlan = optionalInvestmentPlan.get();
        investmentPlanRepository.deleteById(id);
        planTransactionService.addTransaction(savedPlan.getFamilyProfileId(), "Investment",savedPlan.getId(),"Deleted",savedPlan.getGoalAmount(),"");
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

    public List<InvestmentPlanDTO> getPlansByUserId(Long userId) {
        FamilyProfile familyProfile = familyProfileRepository.findByUser_Id(userId);
        List<InvestmentPlan> investmentPlans = investmentPlanRepository.findByFamilyProfileId(familyProfile.getId());
        return investmentPlans.stream()
                .map(investmentPlan -> modelMapper.map(investmentPlan, InvestmentPlanDTO.class))
                .collect(Collectors.toList());
    }
}
