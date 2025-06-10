package com.finwise.service;

import com.finwise.dto.EducationPlanDTO;
import com.finwise.entity.EducationPlan;
import com.finwise.entity.FamilyProfile;
import com.finwise.entity.Child;
import com.finwise.repository.EducationPlanRepository;
import com.finwise.repository.FamilyProfileRepository;
import com.finwise.repository.ChildRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EducationPlanService {

    private final EducationPlanRepository educationPlanRepository;
    private final FamilyProfileRepository familyProfileRepository;
    private final ChildRepository childRepository;
    private final ModelMapper modelMapper;
    private final PlanTransactionService planTransactionService;

    public EducationPlanService(EducationPlanRepository educationPlanRepository,
                                FamilyProfileRepository familyProfileRepository,
                                ChildRepository childRepository,
                                ModelMapper modelMapper,
                                PlanTransactionService planTransactionService) {
        this.educationPlanRepository = educationPlanRepository;
        this.familyProfileRepository = familyProfileRepository;
        this.childRepository = childRepository;
        this.modelMapper = modelMapper;
        this.planTransactionService = planTransactionService;
    }

    public EducationPlanDTO createEducationPlan(Long familyProfileId, @Valid EducationPlanDTO dto) {
        FamilyProfile familyProfile = familyProfileRepository.findById(familyProfileId)
                .orElseThrow(() -> new RuntimeException("Family profile not found"));

        Child child = childRepository.findById(dto.getChildId())
                .orElseThrow(() -> new RuntimeException("Child not found"));

        EducationPlan plan = modelMapper.map(dto, EducationPlan.class);
        plan.setFamilyProfile(familyProfile);
        plan.setChild(child);

// Calculate inflation adjusted cost
        plan.setInflationAdjustedCost(calculateInflationAdjustedCost(
                plan.getEstimatedTotalCost(),
                plan.getEstimatedStartYear() - java.time.LocalDate.now().getYear(),
                plan.getInflationRate()
        ));

        EducationPlan saved = educationPlanRepository.save(plan);
        planTransactionService.addTransaction(familyProfileId,"Education",saved.getId(),"Added",saved.getEstimatedTotalCost(),"");
        return modelMapper.map(saved, EducationPlanDTO.class);
    }

    public EducationPlanDTO getEducationPlan(Long id) {
        EducationPlan plan = educationPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Education plan not found"));
        return modelMapper.map(plan, EducationPlanDTO.class);
    }

    public List<EducationPlanDTO> getPlansByChild(Long childId) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("Child not found"));

        return educationPlanRepository.findByChild(child).stream()
                .map(plan -> modelMapper.map(plan, EducationPlanDTO.class))
                .collect(Collectors.toList());
    }

    public EducationPlanDTO updateEducationPlan(Long id, @Valid EducationPlanDTO dto) {
        EducationPlan existing = educationPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Education plan not found"));

        // Update fields manually
        existing.setPlanName(dto.getPlanName());
        existing.setEducationLevel(dto.getEducationLevel());
        existing.setInstitutionType(dto.getInstitutionType());
        existing.setEstimatedStartYear(dto.getEstimatedStartYear());
        existing.setEstimatedEndYear(dto.getEstimatedEndYear());
        existing.setEstimatedTotalCost(dto.getEstimatedTotalCost());
        existing.setCurrentSavings(dto.getCurrentSavings());
        existing.setMonthlyContribution(dto.getMonthlyContribution());
        existing.setInflationRate(dto.getInflationRate());
        existing.setNotes(dto.getNotes());

        // CRITICAL: Always update the child relationship
        if (dto.getChildId() != null) {
            Child child = childRepository.findById(dto.getChildId())
                    .orElseThrow(() -> new RuntimeException("Child not found"));
            existing.setChild(child);
        }

        // Recalculate inflation adjusted cost
        existing.setInflationAdjustedCost(calculateInflationAdjustedCost(
                existing.getEstimatedTotalCost(),
                existing.getEstimatedStartYear() - java.time.LocalDate.now().getYear(),
                existing.getInflationRate()
        ));

        EducationPlan updated = educationPlanRepository.save(existing);
        planTransactionService.addTransaction(updated.getFamilyProfile().getId(),"Education",updated.getId(),"Upated",updated.getEstimatedTotalCost(),"");
        return convertToDTO(updated);
    }
    private EducationPlanDTO convertToDTO(EducationPlan plan) {
        EducationPlanDTO dto = modelMapper.map(plan, EducationPlanDTO.class);
        if (plan.getChild() != null) {
            dto.setChildId(plan.getChild().getId());
        }
        return dto;
    }

    public void deleteEducationPlan(Long id) {
        if (!educationPlanRepository.existsById(id)) {
            throw new RuntimeException("Education plan not found");
        }
        Optional<EducationPlan> optionalEducationPlan = educationPlanRepository.findById(id);
        EducationPlan deleted = optionalEducationPlan.get();
        planTransactionService.addTransaction(deleted.getFamilyProfile().getId(),"Education",deleted.getId(),"Deleted",deleted.getEstimatedTotalCost(),"");
        educationPlanRepository.deleteById(id);
    }

    public EducationPlanDTO patchEducationPlan(Long id, Map<String, Object> updates) {
        EducationPlan plan = educationPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Education plan not found"));

        updates.forEach((fieldName, fieldValue) -> {
            try {
                Field field = EducationPlan.class.getDeclaredField(fieldName);
                field.setAccessible(true);

                if (field.getType().equals(BigDecimal.class) && fieldValue instanceof Number) {
                    field.set(plan, new BigDecimal(fieldValue.toString()));
                } else if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
                    field.set(plan, Integer.parseInt(fieldValue.toString()));
                } else if (field.getType().equals(String.class)) {
                    field.set(plan, fieldValue.toString());
                }
            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                throw new RuntimeException("Failed to patch field: " + fieldName, e);
            }
        });

// Recalculate inflation adjusted cost if relevant fields were updated
        if (updates.containsKey("estimatedTotalCost") || updates.containsKey("estimatedStartYear") || updates.containsKey("inflationRate")) {
            plan.setInflationAdjustedCost(calculateInflationAdjustedCost(
                    plan.getEstimatedTotalCost(),
                    plan.getEstimatedStartYear() - java.time.LocalDate.now().getYear(),
                    plan.getInflationRate()
            ));
        }

        EducationPlan updated = educationPlanRepository.save(plan);
        planTransactionService.addTransaction(updated.getFamilyProfile().getId(),"Education",updated.getId(),"Upated",updated.getEstimatedTotalCost(),"");
        return modelMapper.map(updated, EducationPlanDTO.class);
    }

    private BigDecimal calculateInflationAdjustedCost(BigDecimal currentCost, int years, BigDecimal inflationRate) {
        if (years <= 0) return currentCost;
        double rate = inflationRate.doubleValue() / 100.0;
        double futureValue = currentCost.doubleValue() * Math.pow(1 + rate, years);
        return BigDecimal.valueOf(futureValue).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public List<EducationPlan> findByFamilyProfile(FamilyProfile familyProfile) {
        return educationPlanRepository.findByChild_FamilyProfile(familyProfile);
    }

    public List<EducationPlanDTO> getEducationPlansByFamily(Long familyId) {
        List<EducationPlan> educationPlans = educationPlanRepository.findByChild_FamilyProfile_Id(familyId);
        return educationPlans.stream()
                .map(educationPlan -> modelMapper.map(educationPlan,EducationPlanDTO.class))
                .collect(Collectors.toList());
    }

    public List<EducationPlanDTO> getPlansByUserId(Long userId) {
        List<EducationPlan> educationPlans = educationPlanRepository.findByChild_FamilyProfile_User_Id(userId);
        return educationPlans.stream()
                .map(educationPlan -> modelMapper.map(educationPlan,EducationPlanDTO.class))
                .collect(Collectors.toList());
    }
}