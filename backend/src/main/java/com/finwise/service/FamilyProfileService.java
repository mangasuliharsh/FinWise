package com.finwise.service;

import com.finwise.dto.FamilyProfileDTO;
import com.finwise.entity.FamilyProfile;
import com.finwise.entity.User;
import com.finwise.repository.FamilyProfileRepository;
import com.finwise.repository.UserRepository;
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
public class FamilyProfileService {

    private final FamilyProfileRepository familyProfileRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public FamilyProfileService(FamilyProfileRepository familyProfileRepository,
                                UserRepository userRepository,
                                ModelMapper modelMapper) {
        this.familyProfileRepository = familyProfileRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public FamilyProfileDTO createFamilyProfile(@Valid FamilyProfileDTO familyProfile) {
        User user = userRepository.findById(familyProfile.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        familyProfile.setUserId(user.getId());
        user.setNewUser(false);
        FamilyProfile saved = familyProfileRepository.save(modelMapper.map(familyProfile, FamilyProfile.class));
        return modelMapper.map(saved, FamilyProfileDTO.class);
    }

    public List<FamilyProfileDTO> getAllFamilyProfiles() {
        return familyProfileRepository.findAll().stream()
                .map(profile -> modelMapper.map(profile, FamilyProfileDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<FamilyProfileDTO> getFamilyProfileById(Long id) {
        return familyProfileRepository.findById(id)
                .map(profile -> modelMapper.map(profile, FamilyProfileDTO.class));
    }

    public FamilyProfileDTO updateFamilyProfile(Long id, FamilyProfileDTO familyProfile) {
        FamilyProfile existing = familyProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        modelMapper.map(familyProfile, existing);

        User user = userRepository.findById(familyProfile.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        existing.setUser(user);

        FamilyProfile updated = familyProfileRepository.save(existing);
        return modelMapper.map(updated, FamilyProfileDTO.class);
    }

    public void deleteFamilyProfile(Long id) {
        if (!familyProfileRepository.existsById(id)) {
            throw new RuntimeException("Profile not found");
        }
        familyProfileRepository.deleteById(id);
    }

    public Optional<FamilyProfileDTO> updateFamilyProfilePartially(Long id, Map<String, Object> updates) {
        Optional<FamilyProfile> optionalProfile = familyProfileRepository.findById(id);

        if (optionalProfile.isEmpty()) {
            return Optional.empty();
        }
//        updates.forEach((field, fieldValue) -> {
//            Field fieldtobeupdated = ReflectionUtils.findRequiredField(FamilyProfile.class,field);
//            if (field != null) {
//                fieldtobeupdated.setAccessible(true);
//                ReflectionUtils.setField(fieldtobeupdated, optionalProfile, fieldValue);
//            }
//        });
//        FamilyProfile saved = familyProfileRepository.save(optionalProfile);
//        return modelMapper.map(saved, FamilyProfileDTO.class);

        FamilyProfile profile = optionalProfile.get();

        updates.forEach((fieldName, fieldValue) -> {
            try {
                Field field = FamilyProfile.class.getDeclaredField(fieldName);
                field.setAccessible(true);

                // Handle BigDecimal conversion
                if (field.getType().equals(BigDecimal.class) && fieldValue instanceof Number) {
                    field.set(profile, new BigDecimal(fieldValue.toString()));
                }
                // Handle Integer fields
                else if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
                    field.set(profile, Integer.parseInt(fieldValue.toString()));
                }
                // Handle String fields
                else if (field.getType().equals(String.class)) {
                    field.set(profile, fieldValue.toString());
                }
                // Handle Boolean fields
                else if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
                    field.set(profile, Boolean.parseBoolean(fieldValue.toString()));
                }
                // You can add more type checks as needed
            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                throw new RuntimeException("Failed to patch field: " + fieldName, e);
            }
        });

        FamilyProfile updatedProfile = familyProfileRepository.save(profile);
        return Optional.of(modelMapper.map(updatedProfile, FamilyProfileDTO.class));
    }

    public void assignUserToFamilyProfile(Long familyProfileId, Long userId) {
        FamilyProfile profile = familyProfileRepository.findById(familyProfileId)
                .orElseThrow(() -> new RuntimeException("FamilyProfile not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        profile.setUser(user);
        familyProfileRepository.save(profile);
    }

    public FamilyProfileDTO getProfileByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        FamilyProfile familyProfile = familyProfileRepository.findFamilyProfileByUserId(userId);
        if (familyProfile == null) {
            return null; // or throw new ResourceNotFoundException("Family profile not found for user: " + userId);
        }
        return modelMapper.map(familyProfile, FamilyProfileDTO.class);
    }
}