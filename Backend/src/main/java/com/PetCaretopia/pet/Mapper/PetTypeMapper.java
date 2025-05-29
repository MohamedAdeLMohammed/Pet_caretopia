package com.PetCaretopia.pet.Mapper;

import com.PetCaretopia.pet.DTO.PetTypeDTO;
import com.PetCaretopia.pet.entity.PetType;

public class PetTypeMapper {
    public static PetTypeDTO toDTO(PetType entity) {
        PetTypeDTO dto = new PetTypeDTO();
        dto.setId(entity.getId());
        dto.setTypeName(entity.getTypeName());
        return dto;
    }
    public static PetType toEntity(PetTypeDTO dto) {
        PetType entity = new PetType();
        entity.setId(dto.getId());
        entity.setTypeName(dto.getTypeName());
        return entity;
    }

}

