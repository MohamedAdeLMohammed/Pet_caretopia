package com.PetCaretopia.pet.controller;

import com.PetCaretopia.pet.DTO.PetBreedDTO;
import com.PetCaretopia.pet.service.PetBreedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pet-breeds")
@RequiredArgsConstructor
public class PetBreedController {

    private final PetBreedService service;

    @PreAuthorize("hasAnyRole('USER', 'SHELTER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<PetBreedDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PreAuthorize("hasAnyRole('USER', 'SHELTER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PetBreedDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PreAuthorize("hasAnyRole('USER', 'SHELTER', 'ADMIN')")
    @GetMapping("/by-type")
    public ResponseEntity<List<PetBreedDTO>> getByType(@RequestParam String type) {
        return ResponseEntity.ok(service.getByType(type));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PetBreedDTO> create(@RequestBody @Valid PetBreedDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PetBreedDTO> update(@PathVariable Long id, @RequestBody @Valid PetBreedDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
