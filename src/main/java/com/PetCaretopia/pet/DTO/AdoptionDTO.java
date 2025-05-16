package com.PetCaretopia.pet.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AdoptionDTO {
    private Long id;

    @NotNull(message = "Pet ID is required")
    private Long petId;

    @NotNull(message = "Adopter ID is required")
    private Long adopterId;

    private Long previousOwnerId;
    private Long shelterId;

    @NotNull(message = "Adoption date is required")
    private LocalDate adoptionDate;

    private Boolean isApproved = false;
}
