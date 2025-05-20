package com.PetCaretopia.pet.Mapper;

import com.PetCaretopia.pet.DTO.AdoptionDTO;
import com.PetCaretopia.pet.entity.Adoption;
import com.PetCaretopia.pet.entity.AdoptionStatus;
import com.PetCaretopia.pet.entity.Pet;
import com.PetCaretopia.pet.entity.Shelter;
import com.PetCaretopia.user.entity.PetOwner;

import java.time.LocalDateTime;

public class AdoptionMapper {

    public static AdoptionDTO toDTO(Adoption entity) {
        AdoptionDTO dto = new AdoptionDTO();
        dto.setId(entity.getId());
        dto.setPetId(entity.getPet().getPetID());
        dto.setAdopterId(entity.getAdopter() != null ? entity.getAdopter().getPetOwnerId() : null);
        dto.setPreviousOwnerId(entity.getPreviousOwner() != null ? entity.getPreviousOwner().getPetOwnerId() : null);
        dto.setShelterId(entity.getShelter() != null ? entity.getShelter().getId() : null);
        dto.setAdoptionDate(entity.getAdoptionDate());
        dto.setStatus(entity.getStatus());
        dto.setPetName(entity.getPet().getPetName());
        dto.setMessage(entity.getMessage());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setRequesterUserId(entity.getRequesterUserId());
        return dto;
    }

    public static Adoption toEntity(
            AdoptionDTO dto,
            Pet pet,
            PetOwner adopter,
            PetOwner previousOwner,
            Shelter shelter
    ) {
        Adoption entity = new Adoption();
        entity.setId(dto.getId());
        entity.setPet(pet);
        if (adopter != null) {
            entity.setAdopter(adopter);
        }
        entity.setPreviousOwner(previousOwner);
        entity.setShelter(shelter);
        entity.setAdoptionDate(dto.getAdoptionDate());
        entity.setStatus(dto.getStatus());
        entity.setMessage(dto.getMessage());
        entity.setCreatedBy(dto.getRequesterUserId());
        entity.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now());
        entity.setRequesterUserId(dto.getRequesterUserId());
        dto.setRequesterUserName(entity.getRequesterUser() != null ? entity.getRequesterUser().getName() : null);
        return entity;
    }

}
