package com.PetCaretopia.pet.controller;

import com.PetCaretopia.Security.Service.CustomUserDetails;
import com.PetCaretopia.pet.DTO.AdoptionDTO;
import com.PetCaretopia.pet.DTO.PetDTO;
import com.PetCaretopia.pet.DTO.ShelterDTO;
import com.PetCaretopia.pet.service.ShelterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shelters")
@RequiredArgsConstructor
public class ShelterController {

    private final ShelterService shelterService;

    @PreAuthorize("hasAnyRole('ADMIN', 'PET_OWNER', 'USER')")
    @GetMapping
    public ResponseEntity<List<ShelterDTO>> getAll() {
        return ResponseEntity.ok(shelterService.getAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PET_OWNER', 'USER')")
    @GetMapping("/{shelterId}/pets")
    public ResponseEntity<List<PetDTO>> getPetsByShelter(@PathVariable Long shelterId) {
        return ResponseEntity.ok(shelterService.getPetsByShelter(shelterId));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{shelterId}/adoptions")
    public ResponseEntity<List<AdoptionDTO>> getAdoptionsByShelter(@PathVariable Long shelterId) {
        return ResponseEntity.ok(shelterService.getAdoptionsByShelter(shelterId));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'PET_OWNER', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ShelterDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(shelterService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShelterDTO> create(
            @RequestBody @Valid ShelterDTO dto,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        return ResponseEntity.ok(shelterService.create(dto, principal));
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
