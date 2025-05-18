package com.PetCaretopia.health.DTO;

import com.PetCaretopia.pet.DTO.PetDTO;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetVaccineDTO {
    private Long petVaccineId;
    private PetDTO pet;
    private VaccineDTO petVaccine;
    private LocalDate vaccinationDate;
    private LocalDate nextDoseDue;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
