package com.PetCaretopia.facility.controller;

import com.PetCaretopia.Security.Service.CustomUserDetails;
import com.PetCaretopia.facility.DTO.AppointmentRequestDTO;
import com.PetCaretopia.facility.entity.AppointmentRequest;
import com.PetCaretopia.facility.service.AppointmentRequestService;
import com.PetCaretopia.facility.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointment-requests")
@RequiredArgsConstructor
public class AppointmentRequestController {
    private final AppointmentRequestService appointmentRequestService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AppointmentRequestDTO>> getAllAppointmentRequestsByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(appointmentRequestService.getAppointmentRequestsByUserId(userId));
    }
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    @GetMapping("/serviceProvider/{serviceProviderId}")
    public ResponseEntity<List<AppointmentRequestDTO>> getAllAppointmentRequestsByServiceProviderId(@PathVariable Long serviceProviderId){
        return ResponseEntity.ok(appointmentRequestService.getAppointmentRequestsByServiceProviderId(serviceProviderId));
    }
    @PreAuthorize("hasAnyRole('ADMIN','SERVICE_PROVIDER')")
    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<List<AppointmentRequestDTO>> getAllAppointmentRequestsByFacilityId(@PathVariable Long facilityId){
        return ResponseEntity.ok(appointmentRequestService.getAppointmentRequestsByFacilityId(facilityId));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<AppointmentRequestDTO>> getAllAppointmentRequests(){
        return ResponseEntity.ok(appointmentRequestService.getAllAppointmentRequests());
    }
    @GetMapping("/status")
    public ResponseEntity<List<AppointmentRequestDTO>> getAllAppointmentRequestsByStatus(@RequestParam AppointmentRequest.AppointmentRequestStatus status){
        return ResponseEntity.ok(appointmentRequestService.getAppointmentRequestsByStatus(status));
    }
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    @PatchMapping("/serviceProvider/{serviceProviderId}/appointmentRequest/{appointmentRequestId}/status")
    public ResponseEntity<AppointmentRequestDTO> updateAppointmentRequestStatusByServiceProviderIdAndAppointmentRequestId(@PathVariable Long serviceProviderId , @PathVariable Long appointmentRequestId, @RequestParam AppointmentRequest.AppointmentRequestStatus status){
        return ResponseEntity.ok(appointmentRequestService.updateAppointmentRequestStatusByServiceProviderIdAndAppointmentRequestId(appointmentRequestId,serviceProviderId,status));
    }
    @PreAuthorize("hasAnyRole('SERVICE_PROVIDER','ADMIN')")
    @PatchMapping("/facility/{facilityId}/appointmentRequest/{appointmentRequestId}/status")
    public ResponseEntity<AppointmentRequestDTO> updateAppointmentRequestStatusByFacilityIdAndAppointmentRequestId(@PathVariable Long facilityId , @PathVariable Long appointmentRequestId , @RequestParam AppointmentRequest.AppointmentRequestStatus status){
        return ResponseEntity.ok(appointmentRequestService.updateAppointmentRequestStatusByFacilityIdAndAppointmentId(facilityId,appointmentRequestId,status));
    }
    @PreAuthorize("hasAnyRole('USER','PET_OWNER')")
    @PutMapping("/user/{userId}/appointmentRequest/{appointmentRequestId}")
    public ResponseEntity<?> updateAppointmentRequestByUserIdAndAppointmentRequestId(@PathVariable Long userId , @PathVariable Long appointmentRequestId , @RequestBody AppointmentRequestDTO dto , @AuthenticationPrincipal CustomUserDetails userDetails){
        Long authId = userDetails.getUserId();
        if(!authId.equals(userId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied !");
        }
        return ResponseEntity.ok(appointmentRequestService.updateAppointmentRequestByUserIdAndAppointmentRequestId(userId,appointmentRequestId,dto));
    }
    @PostMapping("/add/user/{userId}")
    public ResponseEntity<?> createAppointmentRequest(@PathVariable Long userId,@RequestBody AppointmentRequestDTO dto,@AuthenticationPrincipal CustomUserDetails userDetails){
        Long authId = userDetails.getUserId();
        if(!authId.equals(userId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied !");
        }
        return ResponseEntity.ok(appointmentRequestService.createAppointmentRequest(userId,dto));
    }
}
