package com.PetCaretopia.facility.controller;

import com.PetCaretopia.facility.DTO.AppointmentDTO;
import com.PetCaretopia.facility.entity.Appointment;
import com.PetCaretopia.facility.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PreAuthorize("hasAnyRole('ADMIN','SERVICE_PROVIDER')")
    @GetMapping("/all")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments(){
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }
    @GetMapping("user/{userId}")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointmentsByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(appointmentService.getAllAppointmentsByUserId(userId));
    }
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    @GetMapping("/serviceProvider/{serviceProviderId}")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointmentsByServiceProviderId(@PathVariable Long serviceProviderId){
        return ResponseEntity.ok(appointmentService.getAllAppointmentsByServiceProviderId(serviceProviderId));
    }
    @PreAuthorize("hasAnyRole('ADMIN','SERVICE_PROVIDER')")
    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointmentsByServiceFacilityId(@PathVariable Long facilityId){
        return ResponseEntity.ok(appointmentService.getAllAppointmentsByFacilityId(facilityId));
    }
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    @PatchMapping("/serviceProvider/{serviceProviderId}/appointment/{appointmentId}/status")
    public ResponseEntity<AppointmentDTO> markAsTreatedByServiceProviderIdAndAppointmentId(@PathVariable Long serviceProviderId, @PathVariable Long appointmentId , @RequestParam Appointment.AppointmentStatus status){
        return ResponseEntity.ok(appointmentService.markAsTreatedByServiceProviderIdAndAppointmentId(serviceProviderId,appointmentId,status));
    }
}
