package com.PetCaretopia.user.Mapper;

import com.PetCaretopia.user.DTO.PetOwnerDTO;
import com.PetCaretopia.user.entity.PetOwner;
import com.PetCaretopia.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetOwnerMapper {

    private final UserMapper userMapper;

    public PetOwnerDTO toDTO(PetOwner entity) {
        PetOwnerDTO dto = new PetOwnerDTO();
        dto.setPetOwnerId(entity.getPetOwnerId());
        dto.setUser(userMapper.toUserDTO(entity.getUser()));
        return dto;
    }

    public PetOwner toEntity(PetOwnerDTO dto, User user) {
        PetOwner entity = new PetOwner();
        entity.setPetOwnerId(dto.getPetOwnerId());
        entity.setUser(user);
        return entity;
    }
}
