package com.PetCaretopia.facility.DTO;

import com.PetCaretopia.facility.entity.Appointment;
import com.PetCaretopia.user.DTO.ServiceProviderSimpleDTO;
import com.PetCaretopia.user.DTO.UserDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentDTO {
    private Long appointmentId;
    private Long requestId;
    private Long userId;
    private Long serviceProviderId;
    private Long facilityId;
    private String reason;
    private UserDTO user;
    private ServiceProviderSimpleDTO serviceProvider;
    private FacilitySimpleDTO facility;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Appointment.AppointmentStatus appointmentStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm a")
    private LocalDateTime appointmentTime;
}
