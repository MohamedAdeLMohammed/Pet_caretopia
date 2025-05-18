package com.PetCaretopia.health.mapper;

import com.PetCaretopia.health.DTO.VaccineDTO;
import com.PetCaretopia.health.DTO.VaccineTypeDTO;
import com.PetCaretopia.health.entity.Vaccine;
import com.PetCaretopia.health.entity.VaccineType;
import org.springframework.stereotype.Component;

@Component
public class VaccineMapper {
    public VaccineDTO toVaccineDTO(Vaccine vaccine, VaccineType type){
        return new VaccineDTO(
                vaccine.getId(),
                vaccine.getName(),
                vaccine.getDescription(),
                vaccine.getRecommendedAgeWeeks(),
                vaccine.getCreatedAt(),
                vaccine.getUpdatedAt(),
                type
        );
    }
    public Vaccine toVaccine(VaccineDTO dto){
        return new Vaccine(
                dto.getVaccineId(),
                dto.getVaccineName(),
                dto.getVaccineType(),
                dto.getDescription(),
                dto.getRecommendedAgeWeeks(),
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
    }
}
