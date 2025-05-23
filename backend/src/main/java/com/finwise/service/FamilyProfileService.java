package com.finwise.service;

import com.finwise.entity.FamilyProfile;
import com.finwise.repository.FamilyProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FamilyProfileService {

    @Autowired
    private FamilyProfileRepository familyProfileRepository;

    public FamilyProfile createFamilyProfile(FamilyProfile familyProfile) {
        return familyProfileRepository.save(familyProfile);
    }

    public FamilyProfile getFamilyProfile(Long id) {
        return familyProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Family profile not found with id: " + id));
    }

    public List<FamilyProfile> getAllFamilyProfiles() {
        return familyProfileRepository.findAll();
    }

    public FamilyProfile updateFamilyProfile(FamilyProfile familyProfile) {
        getFamilyProfile(familyProfile.getId()); // Check if exists
        return familyProfileRepository.save(familyProfile);
    }
}
