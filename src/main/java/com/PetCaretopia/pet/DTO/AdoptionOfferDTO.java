package com.PetCaretopia.pet.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdoptionOfferDTO {
    @NotNull
    private Long petId;

    @NotNull
    private Long targetUserId;
    
    private String message;
}

