package com.PetCaretopia.pet.controller;

import com.PetCaretopia.pet.DTO.PetTypeDTO;
import com.PetCaretopia.pet.service.PetTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pet-types")
@RequiredArgsConstructor
public class PetTypeController {

    private final PetTypeService service;

    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_PROVIDER', 'USER','PET_OWNER')")
    @GetMapping
    public ResponseEntity<List<PetTypeDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_PROVIDER', 'USER','PET_OWNER')")
    @GetMapping("/{id}")
    public ResponseEntity<PetTypeDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PetTypeDTO> create(@RequestBody @Valid PetTypeDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PetTypeDTO> update(@PathVariable Long id, @RequestBody @Valid PetTypeDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
