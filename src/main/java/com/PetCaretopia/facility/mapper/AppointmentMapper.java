package com.PetCaretopia.facility.mapper;

import com.PetCaretopia.facility.DTO.AppointmentDTO;
import com.PetCaretopia.facility.entity.Appointment;
import com.PetCaretopia.user.mapper.ServiceProviderMapper;
import com.PetCaretopia.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentMapper {
    private final UserMapper userMapper;
    private final ServiceProviderMapper serviceProviderMapper;
    private final FacilityMapper facilityMapper;
    public AppointmentDTO toAppointmentDTO(Appointment appointment){
        return new AppointmentDTO(
                appointment.getId(),
                appointment.getUser().getUserID(),
                appointment.getServiceProvider().getServiceProviderID(),
                appointment.getFacility().getId(),
                userMapper.toUserDTO(appointment.getUser()),
                serviceProviderMapper.toServiceProviderSimpleDTO(appointment.getServiceProvider()),
                facilityMapper.toFacilitySimpleDTO(appointment.getFacility()),
                appointment.getCreatedAt(),
                appointment.getUpdatedAt(),
                appointment.getAppointmentStatus(),
                appointment.getAppointmentTime()
        );
    }
}
