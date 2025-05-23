package com.finwise.controller;

import com.finwise.entity.Child;
import com.finwise.entity.FamilyProfile;
import com.finwise.repository.ChildRepository;
import com.finwise.repository.FamilyProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/children")
@CrossOrigin(origins = "http://localhost:3000")
public class ChildController {

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private FamilyProfileRepository familyProfileRepository;

    @GetMapping
    public List<Child> getAllChildren() {
        return childRepository.findAll();
    }

    @GetMapping("/family/{familyProfileId}")
    public List<Child> getChildrenByFamilyProfile(@PathVariable Long familyProfileId) {
        return childRepository.findByFamilyProfileId(familyProfileId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Child> getChildById(@PathVariable Long id) {
        return childRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/family/{familyProfileId}")
    public ResponseEntity<Child> createChild(@PathVariable Long familyProfileId, @RequestBody Child child) {
        return familyProfileRepository.findById(familyProfileId)
                .map(familyProfile -> {
                    child.setFamilyProfile(familyProfile);
                    Child savedChild = childRepository.save(child);
                    return ResponseEntity.ok(savedChild);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Child> updateChild(@PathVariable Long id, @RequestBody Child childDetails) {
        return childRepository.findById(id)
                .map(child -> {
                    child.setName(childDetails.getName());
                    child.setDateOfBirth(childDetails.getDateOfBirth());
                    child.setCurrentEducationLevel(childDetails.getCurrentEducationLevel());
                    Child updatedChild = childRepository.save(child);
                    return ResponseEntity.ok(updatedChild);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChild(@PathVariable Long id) {
        return childRepository.findById(id)
                .map(child -> {
                    childRepository.delete(child);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
