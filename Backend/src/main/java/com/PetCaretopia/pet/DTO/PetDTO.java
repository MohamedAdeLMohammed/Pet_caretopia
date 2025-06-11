package com.PetCaretopia.pet.DTO;

import com.PetCaretopia.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PetDTO {
    private Long petID;

    @NotBlank(message = "Pet name is required")
    private String petName;

    @NotNull(message = "Pet type is required")
    private String petTypeName;

    @NotNull(message = "Pet breed is required")
    private String petBreedName;

    private Boolean availableForAdoption;
    private Long ownerId;
    private Long shelterId;
    private String imageUrl;

    @NotNull(message = "Pet gender is required")
    private User.Gender gender;
}
