package com.PetCaretopia.pet.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PetTypeDTO {
    private Long id;

    @NotBlank(message = "Pet type name is required")
    private String typeName;
}
