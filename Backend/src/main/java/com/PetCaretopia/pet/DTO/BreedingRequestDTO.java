package com.PetCaretopia.pet.DTO;

import com.PetCaretopia.pet.entity.BreedingStatus;
import com.PetCaretopia.user.DTO.PetOwnerDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class BreedingRequestDTO {
    private Long id;
    private Long malePetId;
    private Long femalePetId;
    private PetDTO malePet;
    private PetDTO femalePet;
    private Long requesterId;
    private Long receiverId;
    private PetOwnerDTO requester;
    private PetOwnerDTO receiver;

    private LocalDate requestDate;
    private BreedingStatus status;
}
