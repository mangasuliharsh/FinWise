package com.finwise.controller;

import com.finwise.dto.ChildDTO;
import com.finwise.service.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/children")
@RequiredArgsConstructor
public class ChildController {

    private final ChildService childService;

    @PostMapping
    public ResponseEntity<ChildDTO> create(@RequestBody ChildDTO dto) {
        return ResponseEntity.ok(childService.createChild(dto));
    }

    @GetMapping
    public ResponseEntity<List<ChildDTO>> getAll() {
        return ResponseEntity.ok(childService.getAllChildren());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChildDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(childService.getChildById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChildDTO> update(@PathVariable Long id, @RequestBody ChildDTO dto) {
        return ResponseEntity.ok(childService.updateChild(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        childService.deleteChild(id);
        return ResponseEntity.noContent().build();
    }
}
