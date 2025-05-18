package com.PetCaretopia.health.mapper;

import com.PetCaretopia.health.DTO.PetVaccineDTO;
import com.PetCaretopia.health.entity.PetVaccine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.PetCaretopia.pet.Mapper.PetMapper;

@Component
@RequiredArgsConstructor
public class PetVaccineMapper {

    private final VaccineMapper vaccineMapper;
    public PetVaccineDTO toPetVaccineDTO(PetVaccine petVaccine){
        return new PetVaccineDTO(
                petVaccine.getId(),
                PetMapper.toDTO(petVaccine.getPet()),
                vaccineMapper.toVaccineDTO(petVaccine.getVaccine(),petVaccine.getVaccine().getType()),
                petVaccine.getVaccinationDate(),
                petVaccine.getNextDoseDue(),
                petVaccine.getNotes(),
                petVaccine.getCreatedAt(),
                petVaccine.getUpdatedAt()
        );
    }
}
