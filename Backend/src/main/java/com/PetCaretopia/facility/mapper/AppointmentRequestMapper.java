package com.PetCaretopia.facility.mapper;

import com.PetCaretopia.facility.DTO.AppointmentRequestDTO;
import com.PetCaretopia.facility.DTO.FacilitySimpleDTO;
import com.PetCaretopia.facility.entity.AppointmentRequest;
import com.PetCaretopia.user.DTO.ServiceProviderSimpleDTO;
import com.PetCaretopia.user.DTO.UserDTO;
import com.PetCaretopia.user.Mapper.ServiceProviderMapper;
import com.PetCaretopia.user.Mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentRequestMapper {
    private final UserMapper userMapper;
    private final ServiceProviderMapper serviceProviderMapper;
    private final FacilityMapper facilityMapper;
    public AppointmentRequestDTO toAppointmentRequestDTO(AppointmentRequest appointmentRequest){
        return new AppointmentRequestDTO(
                appointmentRequest.getId(),
                appointmentRequest.getUser().getUserID(),
                appointmentRequest.getServiceProvider().getServiceProviderID(),
                appointmentRequest.getFacility().getId(),
                appointmentRequest.getReason(),
                appointmentRequest.getCreatedAt(),
                appointmentRequest.getUpdatedAt(),
                appointmentRequest.getRequestedTime(),
                appointmentRequest.getStatus(),
                facilityMapper.toFacilitySimpleDTO(appointmentRequest.getFacility()),
                userMapper.toUserDTO(appointmentRequest.getUser()),
                serviceProviderMapper.toServiceProviderSimpleDTO(appointmentRequest.getServiceProvider())


        );
    }
}
