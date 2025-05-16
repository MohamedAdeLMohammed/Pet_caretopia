package com.PetCaretopia.pet.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PetBreedDTO {
    private Long id;

    @NotBlank(message = "Breed name is required")
    private String breedName;

    @NotNull(message = "Pet type name is required")
    private String petTypeName;
}
