package com.PetCaretopia.pet.controller;

import com.PetCaretopia.Security.Service.CustomUserDetails;
import com.PetCaretopia.pet.DTO.AdoptionDTO;
import com.PetCaretopia.pet.service.AdoptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adoptions")
@RequiredArgsConstructor
public class AdoptionController {

    private final AdoptionService adoptionService;

    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER')")
    @PostMapping
    public ResponseEntity<AdoptionDTO> submit(@RequestBody @Valid AdoptionDTO dto,
                                              @AuthenticationPrincipal CustomUserDetails principal) {
        dto.setAdopterId(principal.getUserId());
        return ResponseEntity.ok(adoptionService.submitRequest(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER')")
    @GetMapping
    public ResponseEntity<List<AdoptionDTO>> getAll() {
        return ResponseEntity.ok(adoptionService.getAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER', 'PET_OWNER')")
    @GetMapping("/{id}")
    public ResponseEntity<AdoptionDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(adoptionService.getById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<AdoptionDTO> approve(@PathVariable Long id) {
        return ResponseEntity.ok(adoptionService.approve(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER')")
    @PutMapping("/{id}/reject")
    public ResponseEntity<AdoptionDTO> reject(@PathVariable Long id) {
        return ResponseEntity.ok(adoptionService.reject(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adoptionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER', 'PET_OWNER')")
    @GetMapping("/by-adopter/{adopterId}")
    public ResponseEntity<List<AdoptionDTO>> getByAdopter(@PathVariable Long adopterId) {
        return ResponseEntity.ok(adoptionService.getByAdopter(adopterId));
    }
}
