package com.PetCaretopia.pet.Mapper;

import com.PetCaretopia.pet.DTO.BreedingRequestDTO;
import com.PetCaretopia.pet.entity.BreedingRequest;
import com.PetCaretopia.user.Mapper.PetOwnerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BreedingRequestMapper {
    private final PetOwnerMapper petOwnerMapper;

    public BreedingRequestDTO toDTO(BreedingRequest entity) {
        BreedingRequestDTO dto = new BreedingRequestDTO();
        dto.setId(entity.getId());
        dto.setMalePetId(entity.getMalePet().getPetID());
        dto.setFemalePetId(entity.getFemalePet().getPetID());
        dto.setRequesterId(entity.getRequester().getPetOwnerId());
        dto.setReceiverId(entity.getReceiver().getPetOwnerId());
        dto.setRequestDate(entity.getRequestDate());
        dto.setStatus(entity.getStatus());

        dto.setMalePet(PetMapper.toDTO(entity.getMalePet()));
        dto.setFemalePet(PetMapper.toDTO(entity.getFemalePet()));
        dto.setRequester(petOwnerMapper.toDTO(entity.getRequester())); // ✅
        dto.setReceiver(petOwnerMapper.toDTO(entity.getReceiver()));   // ✅

        return dto;
    }
}
