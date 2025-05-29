package com.PetCaretopia.facility.mapper;

import com.PetCaretopia.facility.DTO.FacilityDTO;
import com.PetCaretopia.facility.DTO.FacilitySimpleDTO;
import com.PetCaretopia.facility.entity.Facility;
import com.PetCaretopia.user.DTO.ServiceProviderSimpleDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FacilityMapper {
    public FacilityDTO toFacilityDTO(Facility facility, ServiceProviderSimpleDTO serviceProvider){
        return new FacilityDTO(
               facility.getId(),
                facility.getName(),
                facility.getDescription(),
                facility.getAddress(),
                facility.getStatus(),
                facility.getFacilityType(),
                facility.getOpeningTime(),
                facility.getClosingTime(),
                facility.getCreatedAt(),
                facility.getUpdatedAt(),
                serviceProvider
        );
    }
    public FacilitySimpleDTO toFacilitySimpleDTO(Facility facility){
        return new FacilitySimpleDTO(
                facility.getId(),
                facility.getName(),
                facility.getDescription(),
                facility.getAddress(),
                facility.getStatus(),
                facility.getFacilityType(),
                facility.getOpeningTime(),
                facility.getClosingTime(),
                facility.getCreatedAt(),
                facility.getUpdatedAt()
        );
    }
    public Facility toFacility(FacilitySimpleDTO dto){
        return Facility.builder()
                .id(dto.getFacilityId()) // usually just the ID is needed for linking
                .name(dto.getFacilityName())
                .description(dto.getFacilityDescription())
                .address(dto.getFacilityAddress())
                .status(dto.getStatus())
                .facilityType(dto.getFacilityType())
                .openingTime(dto.getOpeningTime())
                .closingTime(dto.getClosingTime())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
