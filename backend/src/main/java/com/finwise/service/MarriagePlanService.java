package com.finwise.service;

import com.finwise.dto.MarriagePlanDTO;
import com.finwise.entity.FamilyProfile;
import com.finwise.entity.MarriagePlan;
import com.finwise.repository.FamilyProfileRepository;
import com.finwise.repository.MarriagePlanRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MarriagePlanService {

    private final MarriagePlanRepository marriagePlanRepository;
    private final FamilyProfileRepository familyProfileRepository;
    private final ModelMapper modelMapper;

    public MarriagePlanService(MarriagePlanRepository marriagePlanRepository, FamilyProfileRepository familyProfileRepository, ModelMapper modelMapper) {
        this.marriagePlanRepository = marriagePlanRepository;
        this.familyProfileRepository = familyProfileRepository;
        this.modelMapper = modelMapper;
    }

    public MarriagePlanDTO createMarriagePlan(Long familyProfileId, MarriagePlanDTO dto) {
        FamilyProfile profile = familyProfileRepository.findById(familyProfileId)
                .orElseThrow(() -> new RuntimeException("Family Profile not found"));

        MarriagePlan plan = modelMapper.map(dto, MarriagePlan.class);
        plan.setFamilyProfile(profile);

        return modelMapper.map(marriagePlanRepository.save(plan), MarriagePlanDTO.class);
    }

    public MarriagePlanDTO getMarriagePlan(Long id) {
        MarriagePlan plan = marriagePlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marriage Plan not found"));
        return modelMapper.map(plan, MarriagePlanDTO.class);
    }

    public List<MarriagePlanDTO> getAllPlansByFamily(Long familyProfileId) {
        return marriagePlanRepository.findByFamilyProfileId(familyProfileId)
                .stream()
                .map(plan -> modelMapper.map(plan, MarriagePlanDTO.class))
                .collect(Collectors.toList());
    }

    public void deleteMarriagePlan(Long id) {
        if (!marriagePlanRepository.existsById(id)) {
            throw new RuntimeException("Marriage Plan not found");
        }
        marriagePlanRepository.deleteById(id);
    }

    public MarriagePlanDTO updateMarriagePlan(Long id, MarriagePlanDTO dto) {
        MarriagePlan existing = marriagePlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marriage Plan not found"));

        dto.setId(id);
        modelMapper.map(dto, existing);  // Copy all non-null values from DTO to entity
        return modelMapper.map(marriagePlanRepository.save(existing), MarriagePlanDTO.class);
    }

    public MarriagePlanDTO patchMarriagePlan(Long id, Map<String, Object> updates) {
        MarriagePlan plan = marriagePlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marriage plan not found"));

        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findRequiredField(MarriagePlan.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, plan, value);
            }
        });

        return modelMapper.map(plan, MarriagePlanDTO.class);
    }

    public List<MarriagePlan> findByFamilyProfile(FamilyProfile familyProfile) {
        return marriagePlanRepository.findByFamilyProfile(familyProfile);
    }
}
