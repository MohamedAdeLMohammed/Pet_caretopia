package com.PetCaretopia.pet.controller;

import com.PetCaretopia.Security.Service.CustomUserDetails;
import com.PetCaretopia.pet.DTO.BreedingRequestDTO;
import com.PetCaretopia.pet.DTO.PetDTO;
import com.PetCaretopia.pet.Mapper.PetMapper;
import com.PetCaretopia.pet.entity.Pet;
import com.PetCaretopia.pet.service.BreedingRequestService;
import com.PetCaretopia.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/breeding-requests")
@RequiredArgsConstructor
public class BreedingRequestController {

    private final BreedingRequestService service;
    private final PetService petService;

    @PostMapping
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<BreedingRequestDTO> create(
            @RequestBody BreedingRequestDTO dto,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        BreedingRequestDTO created = service.createRequest(
                dto.getMalePetId(),
                dto.getFemalePetId(),
                principal.getUsername() // ← username = email هنا
        );
        return ResponseEntity.ok(created);
    }

    @PreAuthorize("hasRole('PET_OWNER')")
    @GetMapping("/incoming")
    public ResponseEntity<List<BreedingRequestDTO>> getIncoming(@AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(service.getRequestsForUser(principal.getUsername()));
    }

    @PreAuthorize("hasRole('PET_OWNER')")
    @GetMapping("/sent")
    public ResponseEntity<List<BreedingRequestDTO>> getSent(@AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(service.getSentRequestsForUser(principal.getUsername()));
    }
    @PreAuthorize("hasRole('PET_OWNER')")
    @PutMapping("/{id}/accept")
    public ResponseEntity<Void> accept(@PathVariable Long id) {
        service.acceptRequest(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('PET_OWNER')")
    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id) {
        service.rejectRequest(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/available-for-breeding")
    public ResponseEntity<List<PetDTO>> getAvailablePetsForBreeding() {
        List<Pet> pets = service.getAllAvailablePetsForBreeding();
        List<PetDTO> result = pets.stream().map(PetMapper::toDTO).toList();
        return ResponseEntity.ok(result);
    }


    @PreAuthorize("hasRole('PET_OWNER')")
    @PutMapping("/{id}/make-available-for-breeding")
    public ResponseEntity<Void> makeAvailableForBreeding(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        petService.makePetAvailableForBreeding(id, principal.getUsername());
        return ResponseEntity.ok().build();
    }

}

