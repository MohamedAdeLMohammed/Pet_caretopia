package com.PetCaretopia.pet.controller;

import com.PetCaretopia.Security.Service.CustomUserDetails;
import com.PetCaretopia.pet.DTO.PetDTO;
import com.PetCaretopia.pet.service.PetService;
import com.PetCaretopia.user.entity.PetOwner;
import com.PetCaretopia.user.repository.PetOwnerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;
    private final PetOwnerRepository petOwnerRepository;

    @PreAuthorize("hasAnyRole('PET_OWNER','USER','SHELTER')")
    @PostMapping
    public ResponseEntity<PetDTO> createPet(
            @RequestPart("pet") @Valid PetDTO dto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Long userId = principal.getUserId();

        PetOwner owner = petOwnerRepository.findByUser_UserID(userId)
                .orElseThrow(() -> new IllegalArgumentException("PetOwner not found for user"));

        dto.setOwnerId(owner.getPetOwnerId()); // ✅ هنا الحل

        return ResponseEntity.ok(petService.createPet(dto, imageFile));
    }




    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER', 'USER')")
    @GetMapping
    public ResponseEntity<List<PetDTO>> getAllPets() {
        return ResponseEntity.ok(petService.getAllPets());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<PetDTO> getPet(@PathVariable Long id) {
        return ResponseEntity.ok(petService.getPetById(id));
    }

    @PreAuthorize("hasAnyRole('SHELTER', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PetDTO> updatePet(
            @PathVariable Long id,
            @RequestPart("pet") @Valid @RequestBody PetDTO dto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        dto.setOwnerId(principal.getUserId());
        return ResponseEntity.ok(petService.updatePet(id, dto, imageFile));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER', 'USER')")
    @GetMapping("/filter")
    public ResponseEntity<List<PetDTO>> filterByType(@RequestParam String type) {
        return ResponseEntity.ok(petService.getPetsByType(type));
    }
}
