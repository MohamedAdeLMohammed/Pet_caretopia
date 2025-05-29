package com.PetCaretopia.facility.DTO;

import com.PetCaretopia.facility.entity.AppointmentRequest;
import com.PetCaretopia.user.DTO.ServiceProviderSimpleDTO;
import com.PetCaretopia.user.DTO.UserDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentRequestDTO {
    private Long requestId;
    private Long userId;
    private Long serviceProviderId;
    private Long facilityId;
    private String requestReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm a")
    private LocalDateTime requestedTime;
    private AppointmentRequest.AppointmentRequestStatus status;
    private FacilitySimpleDTO facility;
    private UserDTO user;
    private ServiceProviderSimpleDTO serviceProvider;
}
