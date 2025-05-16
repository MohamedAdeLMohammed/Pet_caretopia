package com.PetCaretopia.pet.Mapper;

import com.PetCaretopia.pet.DTO.PetBreedDTO;
import com.PetCaretopia.pet.entity.PetBreed;
import com.PetCaretopia.pet.entity.PetType;

public class PetBreedMapper {
    public static PetBreedDTO toDTO(PetBreed entity) {
        PetBreedDTO dto = new PetBreedDTO();
        dto.setId(entity.getId());
        dto.setBreedName(entity.getBreedName());
        dto.setPetTypeName(entity.getPetType().getTypeName());
        return dto;
    }
    public static PetBreed toEntity(PetBreedDTO dto, PetType type) {
        PetBreed entity = new PetBreed();
        entity.setId(dto.getId());
        entity.setBreedName(dto.getBreedName());
        entity.setPetType(type);
        return entity;
    }

}
