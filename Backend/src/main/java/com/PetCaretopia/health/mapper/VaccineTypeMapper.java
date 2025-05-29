package com.PetCaretopia.health.mapper;

import com.PetCaretopia.health.DTO.VaccineTypeDTO;
import com.PetCaretopia.health.entity.VaccineType;
import org.springframework.stereotype.Component;

@Component
public class VaccineTypeMapper {
    public VaccineTypeDTO toVaccineTypeDTO(VaccineType vaccineType){
        return new VaccineTypeDTO(
                vaccineType.getId(),
                vaccineType.getName(),
                vaccineType.getDescription(),
                vaccineType.getUpdatedAt(),
                vaccineType.getCreatedAt()
        );
    }
}
