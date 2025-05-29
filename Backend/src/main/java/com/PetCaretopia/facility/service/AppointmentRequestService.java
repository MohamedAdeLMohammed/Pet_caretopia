package com.PetCaretopia.facility.service;

import com.PetCaretopia.facility.DTO.AppointmentRequestDTO;
import com.PetCaretopia.facility.entity.AppointmentRequest;
import com.PetCaretopia.facility.event.AppointmentRequestAcceptedEvent;
import com.PetCaretopia.facility.mapper.AppointmentRequestMapper;
import com.PetCaretopia.facility.repository.AppointmentRequestRepository;
import com.PetCaretopia.facility.repository.FacilityRepository;
import com.PetCaretopia.user.repository.ServiceProviderRepository;
import com.PetCaretopia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentRequestService {
    private final AppointmentRequestRepository appointmentRequestRepository;
    private final AppointmentRequestMapper appointmentRequestMapper;
    private final UserRepository userRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final FacilityRepository facilityRepository;
    private final ApplicationEventPublisher eventPublisher;


    public AppointmentRequestDTO createAppointmentRequest(Long userId,AppointmentRequestDTO dto){
        var user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("User Not Found !"));
        var serviceProvider = serviceProviderRepository.findById(dto.getServiceProviderId()).orElseThrow(()->new IllegalArgumentException("Service Provider Not Found !"));
        var facility = facilityRepository.findById(dto.getFacilityId()).orElseThrow(()->new IllegalArgumentException("Facility Not Found !"));
        AppointmentRequest request = AppointmentRequest.builder()
                .user(user)
                .serviceProvider(serviceProvider)
                .facility(facility)
                .requestedTime(dto.getRequestedTime())
                .reason(dto.getRequestReason())
                .status(AppointmentRequest.AppointmentRequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        appointmentRequestRepository.save(request);
        return appointmentRequestMapper.toAppointmentRequestDTO(request);
    }
    public List<AppointmentRequestDTO> getAppointmentRequestsByUserId(Long userId){
        var user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("User Not Found !"));
        var requests = appointmentRequestRepository.findByUser_UserID(userId);
        return requests.stream().map(
                appointmentRequestMapper::toAppointmentRequestDTO
        ).collect(Collectors.toList());
    }
    public List<AppointmentRequestDTO> getAppointmentRequestsByServiceProviderId(Long serviceProviderId){
        var serviceProvider = serviceProviderRepository.findById(serviceProviderId).orElseThrow(()->new IllegalArgumentException("Service Provider Not Found !"));
        var requests = appointmentRequestRepository.findByServiceProvider_ServiceProviderID(serviceProviderId);
        return requests.stream().map(
                appointmentRequestMapper::toAppointmentRequestDTO
        ).collect(Collectors.toList());
    }
    public List<AppointmentRequestDTO> getAppointmentRequestsByFacilityId(Long facilityId){
        var facility = facilityRepository.findById(facilityId).orElseThrow(()->new IllegalArgumentException("Facility Not Found !"));
        var requests = appointmentRequestRepository.findByFacility_Id(facilityId);
        return requests.stream().map(
                appointmentRequestMapper::toAppointmentRequestDTO
        ).collect(Collectors.toList());
    }
    public List<AppointmentRequestDTO> getAllAppointmentRequests(){
        var requests = appointmentRequestRepository.findAll();
        return requests.stream().map(
                appointmentRequestMapper::toAppointmentRequestDTO
        ).collect(Collectors.toList());
    }
    public List<AppointmentRequestDTO> getAppointmentRequestsByStatus(AppointmentRequest.AppointmentRequestStatus status){
        var requests = appointmentRequestRepository.findByStatus(status);
        return requests.stream().map(
                appointmentRequestMapper::toAppointmentRequestDTO
        ).collect(Collectors.toList());
    }
    public AppointmentRequestDTO updateAppointmentRequestStatusByServiceProviderIdAndAppointmentRequestId(Long appointmentRequestId ,Long serviceProviderId, AppointmentRequest.AppointmentRequestStatus status){
        var request = appointmentRequestRepository.findById(appointmentRequestId).orElseThrow(()->new IllegalArgumentException("Request Not Found !"));
        var serviceProvider = serviceProviderRepository.findById(serviceProviderId).orElseThrow(()->new IllegalArgumentException("Service Provider Not Found !"));
        if(!request.getServiceProvider().getServiceProviderID().equals(serviceProvider.getServiceProviderID())){
            throw new IllegalArgumentException("You Cannot modify this appointment request because you are not the requested service provider !");
        }
        if(!request.getStatus().equals(AppointmentRequest.AppointmentRequestStatus.PENDING)){
            throw new IllegalArgumentException("This appointment request cannot be modified because it has already been "+request.getStatus().name()+" !");
        }
        if(status.equals(AppointmentRequest.AppointmentRequestStatus.CANCELED)){
            throw new IllegalArgumentException("Only user can cancel his appointment request but you can reject him instead of cancel !");
        }
        if(status.equals(AppointmentRequest.AppointmentRequestStatus.ACCEPTED)){
            eventPublisher.publishEvent(
                    new AppointmentRequestAcceptedEvent(this,request)
            );
        }
        request.setStatus(status);
        request.setUpdatedAt(LocalDateTime.now());
        appointmentRequestRepository.save(request);
        return appointmentRequestMapper.toAppointmentRequestDTO(request);
    }
    public AppointmentRequestDTO updateAppointmentRequestByUserIdAndAppointmentRequestId(Long userId , Long appointmentRequestId, AppointmentRequestDTO dto){
        var existRequest = appointmentRequestRepository.findById(appointmentRequestId).orElseThrow(()->new IllegalArgumentException("Request Not Found !"));
        var user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("User Not Found !"));
        if(!existRequest.getUser().getUserID().equals(user.getUserID())){
            throw new IllegalArgumentException("This appointment request is managed by another user !");
        }
        if(!existRequest.getStatus().equals(AppointmentRequest.AppointmentRequestStatus.PENDING)){
            throw new IllegalArgumentException("This appointment request cannot be modified because it has already been "+existRequest.getStatus().name()+" !");
        }
        if(dto.getUserId() != null && !dto.getUserId().equals(userId)){
            throw new IllegalArgumentException("You Cannot Change User !");
        }
        if(dto.getRequestReason() != null){
            existRequest.setReason(dto.getRequestReason());
        }
        if(dto.getRequestedTime() != null){
            existRequest.setRequestedTime(dto.getRequestedTime());
        }
        if(dto.getServiceProviderId() != null){
            var serviceProvider = serviceProviderRepository.findById(dto.getServiceProviderId()).orElseThrow(()->new IllegalArgumentException("Service Provider Not Found !"));
            existRequest.setServiceProvider(serviceProvider);
        }
        if(dto.getFacilityId() != null){
            var facility = facilityRepository.findById(dto.getFacilityId()).orElseThrow(()->new IllegalArgumentException("Facility Not Found !"));
            existRequest.setFacility(facility);
        }
        if(dto.getStatus().equals(AppointmentRequest.AppointmentRequestStatus.CANCELED)){
            existRequest.setStatus(AppointmentRequest.AppointmentRequestStatus.CANCELED);
        }
        existRequest.setUpdatedAt(LocalDateTime.now());
        appointmentRequestRepository.save(existRequest);
        return appointmentRequestMapper.toAppointmentRequestDTO(existRequest);
    }
    public AppointmentRequestDTO updateAppointmentRequestStatusByFacilityIdAndAppointmentId(Long facilityId,Long appointmentRequestId,AppointmentRequest.AppointmentRequestStatus status){
        var facility = facilityRepository.findById(facilityId).orElseThrow(()->new IllegalArgumentException("Facility Not Found !"));
        var existAppointmentRequest = appointmentRequestRepository.findById(appointmentRequestId).orElseThrow(()->new IllegalArgumentException("Appointment Request Not Found !"));
        if(!facility.getId().equals(existAppointmentRequest.getFacility().getId())){
            throw new IllegalArgumentException("This Appointment is managed by another Facility !");
        }
        if(!existAppointmentRequest.getStatus().equals(AppointmentRequest.AppointmentRequestStatus.PENDING)){
            throw new IllegalArgumentException("This appointment request cannot be modified because it has already been "+existAppointmentRequest.getStatus().name()+" !");
        }
        if(status.equals(AppointmentRequest.AppointmentRequestStatus.CANCELED)){
            throw new IllegalArgumentException("Only user can cancel his appointment request but you can reject him instead of cancel !");
        }
        existAppointmentRequest.setStatus(status);
        existAppointmentRequest.setUpdatedAt(LocalDateTime.now());
        appointmentRequestRepository.save(existAppointmentRequest);
        return appointmentRequestMapper.toAppointmentRequestDTO(existAppointmentRequest);
    }
}
