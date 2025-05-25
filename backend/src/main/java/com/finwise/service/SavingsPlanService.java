package com.finwise.service;


import com.finwise.dto.SavingsPlanDTO;
import com.finwise.entity.SavingsPlan;
import com.finwise.entity.FamilyProfile;
import com.finwise.repository.SavingsPlanRepository;
import com.finwise.repository.FamilyProfileRepository;
import com.finwise.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class SavingsPlanService {

    private final SavingsPlanRepository savingsPlanRepository;
    private final FamilyProfileRepository familyProfileRepository;
    private final ModelMapper modelMapper;

    public SavingsPlanService(SavingsPlanRepository savingsPlanRepository,
                              FamilyProfileRepository familyProfileRepository,
                              ModelMapper modelMapper) {
        this.savingsPlanRepository = savingsPlanRepository;
        this.familyProfileRepository = familyProfileRepository;
        this.modelMapper = modelMapper;
    }

    public SavingsPlanDTO createSavingsPlan(Long familyProfileId, SavingsPlanDTO dto) throws ResourceNotFoundException {
        FamilyProfile familyProfile = familyProfileRepository.findById(familyProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Family profile not found with id: " + familyProfileId));

        SavingsPlan plan = modelMapper.map(dto, SavingsPlan.class);
        plan.setFamilyProfile(familyProfile);
        plan.setCreatedDate(LocalDateTime.now());
        plan.setLastUpdatedDate(LocalDateTime.now());

        SavingsPlan saved = savingsPlanRepository.save(plan);
        return modelMapper.map(saved, SavingsPlanDTO.class);
    }

    public SavingsPlanDTO getSavingsPlan(Long id) throws ResourceNotFoundException {
        SavingsPlan plan = savingsPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Savings plan not found with id: " + id));
        return modelMapper.map(plan, SavingsPlanDTO.class);
    }

    public List<SavingsPlanDTO> getAllPlansByFamily(Long familyProfileId) throws ResourceNotFoundException {
        FamilyProfile familyProfile = familyProfileRepository.findById(familyProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Family profile not found with id: " + familyProfileId));

        return savingsPlanRepository.findByFamilyProfile(familyProfile).stream()
                .map(plan -> modelMapper.map(plan, SavingsPlanDTO.class))
                .collect(Collectors.toList());
    }

    public SavingsPlanDTO updateSavingsPlan(Long id, SavingsPlanDTO dto) throws ResourceNotFoundException {
        SavingsPlan existingPlan = savingsPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Savings plan not found with id: " + id));

        modelMapper.map(dto, existingPlan);
        existingPlan.setLastUpdatedDate(LocalDateTime.now());

        SavingsPlan updated = savingsPlanRepository.save(existingPlan);
        return modelMapper.map(updated, SavingsPlanDTO.class);
    }

    public void deleteSavingsPlan(Long id) throws ResourceNotFoundException {
        if (!savingsPlanRepository.existsById(id)) {
            throw new ResourceNotFoundException("Savings plan not found with id: " + id);
        }
        savingsPlanRepository.deleteById(id);
    }

    public SavingsPlanDTO patchSavingsPlan(Long id, Map<String, Object> updates) throws ResourceNotFoundException {
        SavingsPlan existingPlan = savingsPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Savings plan not found with id: " + id));

        updates.forEach((fieldName, fieldValue) -> {
            try {
                Field field = SavingsPlan.class.getDeclaredField(fieldName);
                field.setAccessible(true);

                if (field.getType().equals(BigDecimal.class) && fieldValue instanceof Number) {
                    field.set(existingPlan, new BigDecimal(fieldValue.toString()));
                } else if (field.getType().equals(String.class)) {
                    field.set(existingPlan, fieldValue.toString());
                } else if (field.getType().equals(LocalDate.class)) {
                    field.set(existingPlan, LocalDate.parse(fieldValue.toString()));
                }
            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                throw new RuntimeException("Failed to patch field: " + fieldName, e);
            }
        });

        existingPlan.setLastUpdatedDate(LocalDateTime.now());
        SavingsPlan updated = savingsPlanRepository.save(existingPlan);
        return modelMapper.map(updated, SavingsPlanDTO.class);
    }
}

