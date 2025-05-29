package com.PetCaretopia.pet.Mapper;

import com.PetCaretopia.pet.DTO.PetDTO;
import com.PetCaretopia.pet.entity.*;
import com.PetCaretopia.user.entity.PetOwner;
import org.springframework.stereotype.Component;

@Component
public class PetMapper {
    public static PetDTO toDTO(Pet pet) {
        PetDTO dto = new PetDTO();
        dto.setPetID(pet.getPetID());
        dto.setPetName(pet.getPetName());
        dto.setOwnerId(pet.getOwner() != null && pet.getOwner().getUser() != null
                ? pet.getOwner().getUser().getUserID()
                : null);

        dto.setShelterId(pet.getShelter() != null ? pet.getShelter().getId() : null);
        dto.setPetTypeName(pet.getPetType().getTypeName());
        dto.setPetBreedName(pet.getPetBreed().getBreedName());
        dto.setImageUrl(pet.getImageUrl());

        return dto;
    }
    public static Pet toEntity(PetDTO dto, PetType type, PetBreed breed, PetOwner owner, Shelter shelter) {
        Pet pet = new Pet();
        pet.setPetID(dto.getPetID());
        pet.setPetName(dto.getPetName());
        pet.setPetType(type);
        pet.setPetBreed(breed);
        pet.setOwner(owner);
        pet.setShelter(shelter);
        pet.setImageUrl(dto.getImageUrl());
        return pet;
    }

}
