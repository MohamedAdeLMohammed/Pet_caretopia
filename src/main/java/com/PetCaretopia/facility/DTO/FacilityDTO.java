package com.PetCaretopia.facility.DTO;

import com.PetCaretopia.facility.entity.Facility;
import com.PetCaretopia.facility.entity.FacilityStatus;
import com.PetCaretopia.user.DTO.ServiceProviderDTO;
import com.PetCaretopia.user.DTO.ServiceProviderSimpleDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacilityDTO {
    private Long facilityId;
    private String facilityName;
    private String facilityDescription;
    private String facilityAddress;
    private FacilityStatus status;
    private Facility.FacilityType facilityType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")
    private LocalTime openingTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")
    private LocalTime closingTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ServiceProviderSimpleDTO> serviceProviders;
}
