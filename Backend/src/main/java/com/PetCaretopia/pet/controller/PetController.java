package com.PetCaretopia.pet.controller;

import com.PetCaretopia.Security.Service.CustomUserDetails;
import com.PetCaretopia.pet.DTO.PetDTO;
import com.PetCaretopia.pet.service.PetService;
import com.PetCaretopia.user.entity.PetOwner;
import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.user.repository.PetOwnerRepository;
import com.PetCaretopia.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;
    private final PetOwnerRepository petOwnerRepository;
    private final UserRepository userRepository;

    @PreAuthorize("hasAnyRole('USER','PET_OWNER','ADMIN')")
    @PostMapping
    public ResponseEntity<PetDTO> createPet(
            @RequestPart("pet") @Valid PetDTO dto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Long userId = principal.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (dto.getShelterId() == null) {
            PetOwner owner = user.getPetOwner();

            if (owner == null) {
                owner = new PetOwner();
                owner.setUser(user);
                user.setPetOwner(owner);
                if(user.getUserRole().equals(User.Role.USER)) {
                    user.setUserRole(User.Role.PET_OWNER);
                }
                user = userRepository.save(user);
                owner = user.getPetOwner();
            }
            dto.setOwnerId(owner.getPetOwnerId());
        } else {
            dto.setAvailableForAdoption(true);
        }

        return ResponseEntity.ok(petService.createPet(dto, imageFile));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_PROVIDER', 'USER','PET_OWNER')")
    @GetMapping
    public ResponseEntity<List<PetDTO>> getAllPets() {
        return ResponseEntity.ok(petService.getAllPets());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_PROVIDER', 'USER','PET_OWNER')")
    @GetMapping("/{id}")
    public ResponseEntity<PetDTO> getPet(@PathVariable Long id) {
        return ResponseEntity.ok(petService.getPetById(id));
    }

    @PreAuthorize("hasAnyRole('PET_OWNER', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PetDTO> updatePet(
            @PathVariable Long id,
            @RequestPart("pet") @Valid PetDTO dto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile,
            @AuthenticationPrincipal CustomUserDetails principal
    ) throws AccessDeniedException {
        Long userId = principal.getUserId();

        PetOwner owner = petOwnerRepository.findByUser_UserID(userId)
                .orElseThrow(() -> new IllegalArgumentException("PetOwner not found for user"));

        dto.setOwnerId(owner.getPetOwnerId());

        return ResponseEntity.ok(petService.updatePet(id, dto, imageFile, principal));
    }


    @PreAuthorize("hasAnyRole('PET_OWNER', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails principal
    ) throws AccessDeniedException {
        petService.deletePet(id, principal);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_PROVIDER', 'USER','PET_OWNER')")
    @GetMapping("/filter")
    public ResponseEntity<List<PetDTO>> filterByType(@RequestParam String type) {
        return ResponseEntity.ok(petService.getPetsByType(type));
    }

    @PreAuthorize("hasAnyRole('PET_OWNER')")
    @GetMapping("/mine")
    public ResponseEntity<List<PetDTO>> getMyPets(@AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(petService.getMyPets(principal.getUserId()));
    }


    @PreAuthorize("hasAnyRole('PET_OWNER','ADMIN')")
    @PutMapping("/{petId}/offer-for-adoption")
    public ResponseEntity<Void> offerForAdoption(@PathVariable Long petId,
                                                 @AuthenticationPrincipal CustomUserDetails principal) throws AccessDeniedException {
        petService.offerForAdoption(petId, principal);
        return ResponseEntity.ok().build();
    }


}
