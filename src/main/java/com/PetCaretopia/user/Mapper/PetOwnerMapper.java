package com.PetCaretopia.user.Mapper;

import com.PetCaretopia.user.DTO.PetOwnerDTO;
import com.PetCaretopia.user.entity.PetOwner;
import com.PetCaretopia.user.entity.User;

public class PetOwnerMapper {
    public static PetOwnerDTO toDTO(PetOwner entity) {
        PetOwnerDTO dto = new PetOwnerDTO();
        dto.setPetOwnerId(entity.getPetOwnerId());
        dto.setUserId(entity.getUser().getUserID());
        return dto;
    }
    public static PetOwner toEntity(PetOwnerDTO dto, User user) {
        PetOwner entity = new PetOwner();
        entity.setPetOwnerId(dto.getPetOwnerId());
        entity.setUser(user);
        return entity;
    }

}
