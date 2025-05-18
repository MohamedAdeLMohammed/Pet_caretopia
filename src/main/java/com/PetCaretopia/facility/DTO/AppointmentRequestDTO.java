package com.PetCaretopia.facility.DTO;

import com.PetCaretopia.facility.entity.AppointmentRequest;
import com.PetCaretopia.user.DTO.ServiceProviderSimpleDTO;
import com.PetCaretopia.user.DTO.UserDTO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentRequestDTO {
    private Long requestId;
    private String requestReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AppointmentRequest.AppointmentRequestStatus status;
    private FacilitySimpleDTO facility;
    private UserDTO user;
    private ServiceProviderSimpleDTO serviceProvider;
}
