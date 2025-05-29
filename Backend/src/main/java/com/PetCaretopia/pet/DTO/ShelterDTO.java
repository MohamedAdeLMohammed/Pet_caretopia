package com.PetCaretopia.pet.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ShelterDTO {
    private Long id;

    @NotBlank(message = "Shelter name is required")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid contact number")
    private String contactNumber;

    @Email(message = "Invalid email format")
    private String email;

    private Long createdBy;

    private String description;
    private String websiteUrl;
}
