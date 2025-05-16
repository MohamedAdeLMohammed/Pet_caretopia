package com.PetCaretopia.pet.Mapper;

import com.PetCaretopia.pet.DTO.ShelterDTO;
import com.PetCaretopia.pet.entity.Shelter;

public class ShelterMapper {
    public static ShelterDTO toDTO(Shelter entity) {
        ShelterDTO dto = new ShelterDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setLocation(entity.getLocation());
        dto.setContactNumber(entity.getContactNumber());
        dto.setEmail(entity.getEmail());
        dto.setDescription(entity.getDescription());
        dto.setWebsiteUrl(entity.getWebsiteUrl());
        return dto;
    }
    public static Shelter toEntity(ShelterDTO dto) {
        Shelter entity = new Shelter();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setLocation(dto.getLocation());
        entity.setContactNumber(dto.getContactNumber());
        entity.setEmail(dto.getEmail());
        entity.setDescription(dto.getDescription());
        entity.setWebsiteUrl(dto.getWebsiteUrl());
        return entity;
    }

}
