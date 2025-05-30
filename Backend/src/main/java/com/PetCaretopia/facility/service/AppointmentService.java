package com.PetCaretopia.facility.service;

import com.PetCaretopia.facility.DTO.AppointmentDTO;
import com.PetCaretopia.facility.entity.Appointment;
import com.PetCaretopia.facility.entity.AppointmentRequest;
import com.PetCaretopia.facility.mapper.AppointmentMapper;
import com.PetCaretopia.facility.repository.AppointmentRepository;
import com.PetCaretopia.facility.repository.AppointmentRequestRepository;
import com.PetCaretopia.facility.repository.FacilityRepository;
import com.PetCaretopia.user.repository.ServiceProviderRepository;
import com.PetCaretopia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRequestRepository appointmentRequestRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final UserRepository userRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final FacilityRepository facilityRepository;

    public void generateAppointment(AppointmentRequest request){

            boolean exists = appointmentRepository.existsByUser_UserIDAndFacility_IdAndServiceProvider_ServiceProviderIDAndAppointmentTimeBetween(
                    request.getUser().getUserID(),
                    request.getFacility().getId(),
                    request.getServiceProvider().getServiceProviderID(),
                    request.getRequestedTime().toLocalDate().atStartOfDay(),
                    request.getRequestedTime().toLocalDate().atTime(23, 59, 59)
            );
            if(!exists){
                Appointment appointment = Appointment.builder()
                        .user(request.getUser())
                        .serviceProvider(request.getServiceProvider())
                        .facility(request.getFacility())
                        .appointmentTime(request.getRequestedTime())
                        .appointmentStatus(Appointment.AppointmentStatus.NOT_TREATED)
                        .reason(request.getReason())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                appointmentRepository.save(appointment);
            }
        }

    public List<AppointmentDTO> getAllAppointments(){
        var appointments = appointmentRepository.findAll();
        return appointments.stream().map(
                appointmentMapper::toAppointmentDTO
        ).collect(Collectors.toList());
    }
    public List<AppointmentDTO> getAllAppointmentsByUserId(Long userId){
        var user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("User Not Found !"));
        var appointments = appointmentRepository.findByUser_UserID(userId);
        return appointments.stream().map(
                appointmentMapper::toAppointmentDTO
        ).collect(Collectors.toList());
    }
    public List<AppointmentDTO> getAllAppointmentsByServiceProviderId(Long serviceProviderId){
        var serviceProvider = serviceProviderRepository.findById(serviceProviderId).orElseThrow(()->new IllegalArgumentException("Service Provider Not Found !"));
        var appointments = appointmentRepository.findByServiceProvider_ServiceProviderID(serviceProviderId);
        return appointments.stream().map(
                appointmentMapper::toAppointmentDTO
        ).collect(Collectors.toList());
    }
    public List<AppointmentDTO> getAllAppointmentsByFacilityId(Long facilityId){
        var facility = facilityRepository.findById(facilityId).orElseThrow(()->new IllegalArgumentException("Facility Not Found !"));
        var appointments = appointmentRepository.findByFacility_Id(facilityId);
        return appointments.stream().map(
                appointmentMapper::toAppointmentDTO
        ).collect(Collectors.toList());
    }
    public AppointmentDTO markAsTreatedByServiceProviderIdAndAppointmentId(Long serviceProviderId,Long appointmentId,Appointment.AppointmentStatus status){
        var serviceProvider = serviceProviderRepository.findById(serviceProviderId).orElseThrow(()->new IllegalArgumentException("Service Provider Not Found !"));
        var appointment = appointmentRepository.findById(appointmentId).orElseThrow(()->new IllegalArgumentException("Appointment Not Found !"));
        if(!serviceProvider.getServiceProviderID().equals(appointment.getServiceProvider().getServiceProviderID())){
            throw new IllegalArgumentException("This Appointment is managed by another service provider !");
        }
        if(appointment.getAppointmentStatus().equals(Appointment.AppointmentStatus.TREATED)){
            throw new IllegalArgumentException("This Appointment is already Treated !");
        }
        appointment.setAppointmentStatus(Appointment.AppointmentStatus.TREATED);
        appointment.setUpdatedAt(LocalDateTime.now());
        appointmentRepository.save(appointment);
        return appointmentMapper.toAppointmentDTO(appointment);
    }
    public AppointmentDTO getAppointmentById(Long appointmentId){
        var appointment = appointmentRepository.findById(appointmentId).orElseThrow(()->new IllegalArgumentException("Not Found !"));
        return appointmentMapper.toAppointmentDTO(appointment);
    }
}
