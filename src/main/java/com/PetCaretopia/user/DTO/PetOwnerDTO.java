package com.PetCaretopia.user.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PetOwnerDTO {
    private Long petOwnerId;

    @NotNull(message = "User ID is required")
    private Long userId;
}
