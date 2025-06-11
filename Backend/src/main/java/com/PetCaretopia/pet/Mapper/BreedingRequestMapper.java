package com.PetCaretopia.pet.Mapper;

import com.PetCaretopia.pet.DTO.BreedingRequestDTO;
import com.PetCaretopia.pet.entity.BreedingRequest;
import org.springframework.stereotype.Component;

@Component
public class BreedingRequestMapper {
    public BreedingRequestDTO toDTO(BreedingRequest request) {
        BreedingRequestDTO dto = new BreedingRequestDTO();
        dto.setId(request.getId());
        dto.setMalePetId(request.getMalePet().getPetID());
        dto.setFemalePetId(request.getFemalePet().getPetID());
        dto.setRequesterId(request.getRequester().getPetOwnerId());
        dto.setReceiverId(request.getReceiver().getPetOwnerId());
        dto.setRequestDate(request.getRequestDate());
        dto.setStatus(request.getStatus());
        return dto;
    }
}
