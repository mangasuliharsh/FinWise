package com.finwise.controller;

import com.finwise.entity.FamilyProfile;
import com.finwise.service.FamilyProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/family-profiles")
@CrossOrigin(origins = "*")
public class FamilyProfileController {

    @Autowired
    private FamilyProfileService familyProfileService;

    @PostMapping
    public ResponseEntity<FamilyProfile> createFamilyProfile(@RequestBody FamilyProfile familyProfile) {
        return ResponseEntity.ok(familyProfileService.createFamilyProfile(familyProfile));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FamilyProfile> getFamilyProfile(@PathVariable Long id) {
        return ResponseEntity.ok(familyProfileService.getFamilyProfile(id));
    }

    @GetMapping
    public ResponseEntity<List<FamilyProfile>> getAllFamilyProfiles() {
        return ResponseEntity.ok(familyProfileService.getAllFamilyProfiles());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FamilyProfile> updateFamilyProfile(
            @PathVariable Long id,
            @RequestBody FamilyProfile familyProfile) {
        familyProfile.setId(id);
        return ResponseEntity.ok(familyProfileService.updateFamilyProfile(familyProfile));
    }
}
