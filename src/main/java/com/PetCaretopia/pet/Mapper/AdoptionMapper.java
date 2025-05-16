package com.PetCaretopia.pet.Mapper;

import com.PetCaretopia.pet.DTO.AdoptionDTO;
import com.PetCaretopia.pet.entity.Adoption;
import com.PetCaretopia.pet.entity.Pet;
import com.PetCaretopia.pet.entity.Shelter;
import com.PetCaretopia.user.entity.PetOwner;

public class AdoptionMapper {
    public static AdoptionDTO toDTO(Adoption entity) {
        AdoptionDTO dto = new AdoptionDTO();
        dto.setId(entity.getId());
        dto.setPetId(entity.getPet().getPetID());
        dto.setAdopterId(entity.getAdopter().getPetOwnerId());
        dto.setPreviousOwnerId(entity.getPreviousOwner() != null ? entity.getPreviousOwner().getPetOwnerId() : null);
        dto.setShelterId(entity.getShelter() != null ? entity.getShelter().getId() : null);
        dto.setAdoptionDate(entity.getAdoptionDate());
        dto.setIsApproved(entity.getIsApproved());
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
        entity.setAdopter(adopter);
        entity.setPreviousOwner(previousOwner);
        entity.setShelter(shelter);
        entity.setAdoptionDate(dto.getAdoptionDate());
        entity.setIsApproved(dto.getIsApproved());
        return entity;
    }

}