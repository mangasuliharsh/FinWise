// Controller
package com.finwise.controller;

import com.finwise.config.CustomOAuth2SuccessHandler;
import com.finwise.dto.FamilyProfileDTO;
import com.finwise.entity.User;
import com.finwise.service.FamilyProfileService;
import com.finwise.util.Util;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/familyProfile")
public class FamilyProfileController {

    private final FamilyProfileService familyProfileService;
    private final Util util;

    public FamilyProfileController(FamilyProfileService familyProfileService, Util util) {
        this.familyProfileService = familyProfileService;
        this.util = util;
    }



//    @GetMapping
//    public ResponseEntity<List<FamilyProfileDTO>> getAllProfiles() {
//        return ResponseEntity.ok(familyProfileService.getAllFamilyProfiles());
//    }

    @GetMapping("/{id}")
    public ResponseEntity<FamilyProfileDTO> getProfileById(@PathVariable Long id) {
        Optional<FamilyProfileDTO> profile = familyProfileService.getFamilyProfileById(id);
        return profile.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<FamilyProfileDTO> createProfile(@Valid @RequestBody FamilyProfileDTO dto) {
        Long userId = util.getCurrentUserId();
        dto.setUserId(userId);
        return ResponseEntity.status(201).body(familyProfileService.createFamilyProfile(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FamilyProfileDTO> updateProfile(@PathVariable Long id, @Valid @RequestBody FamilyProfileDTO dto) {
        return ResponseEntity.ok(familyProfileService.updateFamilyProfile(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        familyProfileService.deleteFamilyProfile(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<FamilyProfileDTO> updatePartialFamilyProfile(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        Optional<FamilyProfileDTO> updatedProfile = familyProfileService.updateFamilyProfilePartially(id, updates);
        return updatedProfile.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/assign-user/{familyprofileid}/familyprofile{userid}")
    public ResponseEntity<String> assignUserToProfile(@PathVariable Long familyprofileid,@PathVariable Long userid) {
        familyProfileService.assignUserToFamilyProfile(familyprofileid,userid);
        return ResponseEntity.ok("User assigned to Family Profile successfully.");
    }

    @GetMapping
    public ResponseEntity<FamilyProfileDTO> getProfileByUserId(){
        Long userId = util.getCurrentUserId();
        return ResponseEntity.ok(familyProfileService.getProfileByUserId(userId));
    }

}