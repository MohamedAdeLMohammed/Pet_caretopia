package com.PetCaretopia.pet.DTO;

import com.PetCaretopia.pet.entity.BreedingStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class BreedingRequestDTO {
    private Long id;
    private Long malePetId;
    private Long femalePetId;
    private Long requesterId;
    private Long receiverId;
    private LocalDate requestDate;
    private BreedingStatus status;
}
