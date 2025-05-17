package com.PetCaretopia.pet.DTO;

import com.PetCaretopia.pet.entity.AdoptionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AdoptionDTO {
    private Long id;

    @NotNull(message = "Pet ID is required")
    private Long petId;

    private Long adopterId;

    private Long previousOwnerId;
    private Long shelterId;

    private LocalDate adoptionDate;

    private AdoptionStatus status;
    private String message;
    private LocalDateTime createdAt;
    private Long requesterUserId;

}
