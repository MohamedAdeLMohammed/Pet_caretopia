package com.PetCaretopia.facility.service;

import com.PetCaretopia.facility.DTO.FacilityDTO;
import com.PetCaretopia.facility.DTO.FacilitySimpleDTO;
import com.PetCaretopia.facility.entity.*;
import com.PetCaretopia.facility.mapper.FacilityMapper;
import com.PetCaretopia.facility.repository.AppointmentRepository;
import com.PetCaretopia.facility.repository.AppointmentRequestRepository;
import com.PetCaretopia.facility.repository.FacilityRepository;
import com.PetCaretopia.user.DTO.ServiceProviderSimpleDTO;
import com.PetCaretopia.user.Mapper.ServiceProviderMapper;
import com.PetCaretopia.user.entity.ServiceProvider;
import com.PetCaretopia.user.repository.ServiceProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityService {
    private final FacilityRepository facilityRepository;
    private final FacilityMapper facilityMapper;
    private final ServiceProviderMapper serviceProviderMapper;
    private final ServiceProviderRepository serviceProviderRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentRequestRepository appointmentRequestRepository;

    public FacilitySimpleDTO createFacility(Long serviceProviderId,FacilitySimpleDTO dto){
        var serviceProvider = serviceProviderRepository.findById(serviceProviderId).orElseThrow(()->new IllegalArgumentException("Service Provider Not Found !"));
        Facility facility = Facility.builder()
                .name(dto.getFacilityName())
                .address(dto.getFacilityAddress())
                .description(dto.getFacilityDescription())
                .facilityType(dto.getFacilityType())
                .createdAt(LocalDateTime.now())
                .status(FacilityStatus.ACTIVE)
                .updatedAt(LocalDateTime.now())
                .openingTime(dto.getOpeningTime())
                .closingTime(dto.getClosingTime())
                .serviceProvider(serviceProvider)
                .build();
        if(serviceProvider.getServiceProviderType().equals(ServiceProvider.ServiceProviderType.OTHER)){
            throw new IllegalArgumentException("You Must Be Vet , Trainer , or Pet Sitter then you can have your facility !");
        }
        if(serviceProvider.getServiceProviderType().equals(ServiceProvider.ServiceProviderType.VET) && !facility.getFacilityType().equals(Facility.FacilityType.VETERINARY_CLINIC)){
            throw new IllegalArgumentException("Vet can have only Veterinary Clinic !");
        }
        if(serviceProvider.getServiceProviderType().equals(ServiceProvider.ServiceProviderType.TRAINER) && !facility.getFacilityType().equals(Facility.FacilityType.TRAINING_CENTER)){
            throw new IllegalArgumentException("Trainer can have only Training Center !");
        }
        if(serviceProvider.getServiceProviderType().equals(ServiceProvider.ServiceProviderType.SITTER) && !facility.getFacilityType().equals(Facility.FacilityType.PET_HOTEL)){
            throw new IllegalArgumentException("Pet Sitter can have only Pet Hotel !");
        }
        facilityRepository.save(facility);
        return facilityMapper.toFacilitySimpleDTO(facility);
    }
    public FacilitySimpleDTO updateFacility(Long facilityId,FacilitySimpleDTO dto){
        var existFacility = facilityRepository.findById(facilityId).orElseThrow(()-> new IllegalArgumentException("Facility Not Found !"));

        if(dto.getFacilityName() != null){
            existFacility.setName(dto.getFacilityName());
        }
        if(dto.getFacilityDescription() != null){
            existFacility.setDescription(dto.getFacilityDescription());
        }
        if(dto.getFacilityAddress() != null){
            existFacility.setAddress(dto.getFacilityAddress());
        }
        if(dto.getStatus() != null){
            existFacility.setStatus(dto.getStatus());
        }
        if(dto.getOpeningTime() != null){
            existFacility.setOpeningTime(dto.getOpeningTime());
        }
        if(dto.getClosingTime() != null){
            existFacility.setClosingTime(dto.getClosingTime());
        }
        if(existFacility.getServiceProvider().getServiceProviderType().equals(ServiceProvider.ServiceProviderType.VET) && !existFacility.getFacilityType().equals(Facility.FacilityType.VETERINARY_CLINIC)){
            throw new IllegalArgumentException("Vet can have only Veterinary Clinic !");
        }
        if(existFacility.getServiceProvider().getServiceProviderType().equals(ServiceProvider.ServiceProviderType.TRAINER) && !existFacility.getFacilityType().equals(Facility.FacilityType.TRAINING_CENTER)){
            throw new IllegalArgumentException("Trainer can have only Training Center !");
        }
        if(existFacility.getServiceProvider().getServiceProviderType().equals(ServiceProvider.ServiceProviderType.SITTER) && !existFacility.getFacilityType().equals(Facility.FacilityType.PET_HOTEL)){
            throw new IllegalArgumentException("Pet Sitter can have only Pet Hotel !");
        }
        existFacility.setUpdatedAt(LocalDateTime.now());
        facilityRepository.save(existFacility);
        return facilityMapper.toFacilitySimpleDTO(existFacility);
    }
    public String deleteFacilityById(Long id) {
        var facility = facilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Facility Not Found!"));

        var requests = appointmentRequestRepository.findByFacility_Id(id);
        var appointments = appointmentRepository.findByFacility_Id(id);

        boolean hasPendingRequests = requests != null && !requests.isEmpty() && requests.stream()
                .anyMatch(request -> request.getStatus() == AppointmentRequest.AppointmentRequestStatus.PENDING);

        boolean hasNotTreatedAppointments = appointments != null && !appointments.isEmpty() && appointments.stream()
                .anyMatch(appointment -> appointment.getAppointmentStatus() == Appointment.AppointmentStatus.NOT_TREATED);

        if (hasPendingRequests || hasNotTreatedAppointments) {
            throw new IllegalArgumentException("You cannot delete this facility because there are some Requests and Appointments still not handled!");
        }

        if (appointments != null && !appointments.isEmpty()) {
            appointments.forEach(appointmentRepository::delete);
        }

        if (requests != null && !requests.isEmpty()) {
            requests.forEach(appointmentRequestRepository::delete);
        }

        facilityRepository.delete(facility);

        return "Facility Deleted!";
    }
    public FacilitySimpleDTO getFacilityById(Long id){
        var facility = facilityRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Facility Not Found !"));
        return facilityMapper.toFacilitySimpleDTO(facility);
    }
    public FacilityDTO getFacilityWithServiceProvidersByFacilityId(Long id){
        var facility = facilityRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Facility Not Found !"));
        var serviceProvider = facility.getServiceProvider();
        var simpleServiceProvider = serviceProviderMapper.toServiceProviderSimpleDTO(serviceProvider);
        return facilityMapper.toFacilityDTO(facility,simpleServiceProvider);
    }
    public FacilityDTO getFacilityWithServiceProvidersByFacilityName(String facilityName){
        var facility = facilityRepository.findByName(facilityName).orElseThrow(()->new IllegalArgumentException("Facility not found ! "+facilityName));
        var serviceProvider = facility.getServiceProvider();
        var simpleServiceProvider = serviceProviderMapper.toServiceProviderSimpleDTO(serviceProvider);
        return facilityMapper.toFacilityDTO(facility,simpleServiceProvider);
    }
    public List<FacilitySimpleDTO> getFacilitiesByFacilityType(Facility.FacilityType type){
        var facilities = facilityRepository.findByFacilityType(type);
        return facilities.stream().map(facilityMapper::toFacilitySimpleDTO).collect(Collectors.toList());
    }
    public List<FacilitySimpleDTO> getServiceProviderFacilitiesByServiceProviderId(Long serviceProviderId){
        var serviceProvider = serviceProviderRepository.findById(serviceProviderId).orElseThrow(()->new IllegalArgumentException("Service Provider not found !"));
        var facilities = facilityRepository.findFacilitiesByServiceProvider_ServiceProviderID(serviceProviderId);
        return facilities.stream().map(facilityMapper::toFacilitySimpleDTO).collect(Collectors.toList());
    }
    public List<FacilityDTO> getAllFacilitiesWithServiceProviders(){
        var facilities = facilityRepository.findAll();
        return facilities.stream().map(facility ->
                facilityMapper.toFacilityDTO(facility,serviceProviderMapper.toServiceProviderSimpleDTO(facility.getServiceProvider()))).collect(Collectors.toList());
    }

}
