package com.PetCaretopia.pet.controller;

import com.PetCaretopia.Security.Service.CustomUserDetails;
import com.PetCaretopia.pet.DTO.AdoptionDTO;
import com.PetCaretopia.pet.DTO.AdoptionOfferDTO;
import com.PetCaretopia.pet.DTO.PetDTO;
import com.PetCaretopia.pet.entity.AdoptionStatus;
import com.PetCaretopia.pet.service.AdoptionService;
import com.PetCaretopia.pet.service.PetService;
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
    private final PetService petService;
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER')")
    @PostMapping
    public ResponseEntity<AdoptionDTO> submit(@RequestBody @Valid AdoptionDTO dto,
                                              @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(adoptionService.submitRequest(dto, principal));
    }

//    @PreAuthorize("hasAnyRole('PET_OWNER', 'ADMIN','USER')")
//    @PostMapping("/offer")
//    public ResponseEntity<AdoptionDTO> offerAdoption(
//            @RequestBody @Valid AdoptionOfferDTO dto,
//            @AuthenticationPrincipal CustomUserDetails principal
//    ) {
//        return ResponseEntity.ok(adoptionService.offerAdoption(dto, principal));
//    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AdoptionDTO>> getAll() {
        return ResponseEntity.ok(adoptionService.getAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PET_OWNER')")
    @GetMapping("/by-pet/{petId}")
    public ResponseEntity<List<AdoptionDTO>> getByPet(@PathVariable Long petId) {
        return ResponseEntity.ok(adoptionService.getByPetId(petId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','PET_OWNER')")
    @GetMapping("/{id}")
    public ResponseEntity<AdoptionDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(adoptionService.getById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PET_OWNER')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<AdoptionDTO> approve(@PathVariable Long id,
                                               @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(adoptionService.approve(id, principal));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PET_OWNER')")
    @PutMapping("/{id}/reject")
    public ResponseEntity<AdoptionDTO> reject(@PathVariable Long id,
                                              @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(adoptionService.reject(id, principal));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PET_OWNER','USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adoptionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PET_OWNER')")
    @GetMapping("/by-adopter/{adopterId}")
    public ResponseEntity<List<AdoptionDTO>> getByAdopter(@PathVariable Long adopterId) {
        return ResponseEntity.ok(adoptionService.getByAdopter(adopterId));
    }

    @PreAuthorize("hasAnyRole('PET_OWNER')")
    @GetMapping("/mine")
    public ResponseEntity<List<AdoptionDTO>> getRequestsForMyPets(@AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(adoptionService.getByOwnerPets(principal.getUserId()));
    }
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER')")
    @GetMapping("/my-requests")
    public ResponseEntity<List<AdoptionDTO>> getMyAdoptionRequests(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam(value = "status", required = false) AdoptionStatus status
    ) {
        return ResponseEntity.ok(adoptionService.getMyRequests(principal.getUserId(), status));
    }

    @GetMapping("/available-for-adoption")
    public ResponseEntity<List<PetDTO>> getAvailablePets() {
        List<PetDTO> pets = petService.getAvailableForAdoption();
        return ResponseEntity.ok(pets);
    }

}