package com.finwise.service;

import com.finwise.dto.ChildDTO;
import com.finwise.entity.Child;
import com.finwise.entity.FamilyProfile;
import com.finwise.repository.ChildRepository;
import com.finwise.repository.FamilyProfileRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChildService {

    private final ChildRepository childRepository;
    private final FamilyProfileRepository familyProfileRepository;
    private final ModelMapper modelMapper;

    public ChildDTO createChild(ChildDTO dto) {
        Child child = modelMapper.map(dto, Child.class);
        FamilyProfile family = familyProfileRepository.findById(dto.getFamilyProfileId())
                .orElseThrow(() -> new RuntimeException("FamilyProfile not found"));
        child.setFamilyProfile(family);

        Child saved = childRepository.save(child);
        return convertToDTO(saved);
    }

    public List<ChildDTO> getAllChildren() {
        return childRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ChildDTO getChildById(Long id) {
        Child child = childRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Child not found"));
        return convertToDTO(child);
    }

    public ChildDTO updateChild(Long id, ChildDTO dto) {
        Child existing = childRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Child not found"));

        // Preserve family profile relationship
        FamilyProfile currentFamily = existing.getFamilyProfile();

        // Update basic fields manually to preserve relationships
        existing.setName(dto.getName());
        existing.setDateOfBirth(dto.getDateOfBirth());
        existing.setCurrentEducationLevel(dto.getCurrentEducationLevel());

        // Update family profile only if changed
        if (dto.getFamilyProfileId() != null && !currentFamily.getId().equals(dto.getFamilyProfileId())) {
            FamilyProfile newFamily = familyProfileRepository.findById(dto.getFamilyProfileId())
                    .orElseThrow(() -> new RuntimeException("FamilyProfile not found"));
            existing.setFamilyProfile(newFamily);
        }

        return convertToDTO(childRepository.save(existing));
    }

    public void deleteChild(Long id) {
        childRepository.deleteById(id);
    }

    private ChildDTO convertToDTO(Child child) {
        ChildDTO dto = modelMapper.map(child, ChildDTO.class);
        // Ensure familyProfileId is properly set
        if (child.getFamilyProfile() != null) {
            dto.setFamilyProfileId(child.getFamilyProfile().getId());
        }
        return dto;
    }

    public ChildDTO patchChild(Map<String, Object> updates, Long id) {
        Child existingChild = childRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Child not found"));

        // Preserve original family profile
        FamilyProfile originalFamily = existingChild.getFamilyProfile();

        updates.forEach((field, fieldValue) -> {
            if (field.equals("familyProfileId") && fieldValue != null) {
                Long familyProfileId = Long.valueOf(fieldValue.toString());
                FamilyProfile newFamily = familyProfileRepository.findById(familyProfileId)
                        .orElseThrow(() -> new RuntimeException("FamilyProfile not found"));
                existingChild.setFamilyProfile(newFamily);
            } else {
                Field fieldToBeUpdated = ReflectionUtils.findRequiredField(Child.class, field);
                if (fieldToBeUpdated != null) {
                    fieldToBeUpdated.setAccessible(true);
                    ReflectionUtils.setField(fieldToBeUpdated, existingChild, fieldValue);
                }
            }
        });

        // Ensure family profile is not null
        if (existingChild.getFamilyProfile() == null) {
            existingChild.setFamilyProfile(originalFamily);
        }

        Child saved = childRepository.save(existingChild);
        return convertToDTO(saved);
    }
}
