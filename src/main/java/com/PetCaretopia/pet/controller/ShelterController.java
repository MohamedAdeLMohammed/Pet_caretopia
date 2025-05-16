package com.PetCaretopia.pet.controller;

import com.PetCaretopia.pet.DTO.ShelterDTO;
import com.PetCaretopia.pet.service.ShelterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shelters")
@RequiredArgsConstructor
public class ShelterController {

    private final ShelterService shelterService;

    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER', 'USER')")
    @GetMapping
    public ResponseEntity<List<ShelterDTO>> getAll() {
        return ResponseEntity.ok(shelterService.getAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ShelterDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(shelterService.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ShelterDTO> create(@RequestBody @Valid ShelterDTO dto) {
        return ResponseEntity.ok(shelterService.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ShelterDTO> update(@PathVariable Long id, @RequestBody @Valid ShelterDTO dto) {
        return ResponseEntity.ok(shelterService.update(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        shelterService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
