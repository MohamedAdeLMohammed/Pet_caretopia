package com.PetCaretopia.facility.DTO;

import com.PetCaretopia.facility.entity.Appointment;
import com.PetCaretopia.user.DTO.ServiceProviderSimpleDTO;
import com.PetCaretopia.user.DTO.UserDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentDTO {
    private Long AppointmentId;
    private UserDTO user;
    private ServiceProviderSimpleDTO serviceProvider;
    private FacilitySimpleDTO facility;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Appointment.AppointmentStatus appointmentStatus;
    private LocalDateTime appointmentTime;
}
