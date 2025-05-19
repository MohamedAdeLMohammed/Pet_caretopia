package com.PetCaretopia.facility.service;

import com.PetCaretopia.facility.DTO.FacilityDTO;
import com.PetCaretopia.facility.DTO.FacilitySimpleDTO;
import com.PetCaretopia.facility.entity.Facility;
import com.PetCaretopia.facility.entity.FacilityStatus;
import com.PetCaretopia.facility.mapper.FacilityMapper;
import com.PetCaretopia.facility.repository.FacilityRepository;
import com.PetCaretopia.user.DTO.ServiceProviderSimpleDTO;
import com.PetCaretopia.user.Mapper.ServiceProviderMapper;
import com.PetCaretopia.user.entity.ServiceProvider;
import com.PetCaretopia.user.repository.ServiceProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityService {
    private final FacilityRepository facilityRepository;
    private final FacilityMapper facilityMapper;
    private final ServiceProviderMapper serviceProviderMapper;
    private final ServiceProviderRepository serviceProviderRepository;

    public FacilitySimpleDTO createFacility(FacilitySimpleDTO dto){
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
                .build();
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
        if(dto.getFacilityType() != null){
            existFacility.setFacilityType(dto.getFacilityType());
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
        existFacility.setUpdatedAt(LocalDateTime.now());
        facilityRepository.save(existFacility);
        return facilityMapper.toFacilitySimpleDTO(existFacility);
    }
    public String deleteFacilityById(Long id){
        var facility = facilityRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Facility Not Found !"));
        if(!facility.getServiceProviders().isEmpty()){
            throw new IllegalArgumentException("Can not Delete Facility because it has service providers working on it right now !");
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
        List<ServiceProvider> serviceProviders = facility.getServiceProviders();
       List<ServiceProviderSimpleDTO> simpleServiceProviders = serviceProviders.stream().map(serviceProviderMapper::toServiceProviderSimpleDTO).toList();
        return facilityMapper.toFacilityDTO(facility,simpleServiceProviders);
    }
    public FacilityDTO getFacilityWithServiceProvidersByFacilityName(String facilityName){
        var facility = facilityRepository.findByName(facilityName).orElseThrow(()->new IllegalArgumentException("Facility not found ! "+facilityName));
        List<ServiceProvider> serviceProviders = facility.getServiceProviders();
        List<ServiceProviderSimpleDTO> simpleServiceProviders = serviceProviders.stream().map(serviceProviderMapper::toServiceProviderSimpleDTO).toList();
        return facilityMapper.toFacilityDTO(facility,simpleServiceProviders);
    }
    public List<FacilitySimpleDTO> getFacilitiesByFacilityType(Facility.FacilityType type){
        var facilities = facilityRepository.findByFacilityType(type);
        return facilities.stream().map(facilityMapper::toFacilitySimpleDTO).collect(Collectors.toList());
    }
    public List<FacilitySimpleDTO> getServiceProviderFacilitiesByServiceProviderId(Long serviceProviderId){
        var serviceProvider = serviceProviderRepository.findById(serviceProviderId).orElseThrow(()->new IllegalArgumentException("Service Provider not found !"));
        var facilities = facilityRepository.findFacilitiesByServiceProviderID(serviceProviderId);
        return facilities.stream().map(facilityMapper::toFacilitySimpleDTO).collect(Collectors.toList());
    }
    public List<FacilityDTO> getAllFacilitiesWithServiceProviders(){
        var facilities = facilityRepository.findAll();
        return facilities.stream().map(facility ->
                facilityMapper.toFacilityDTO(facility,facility.getServiceProviders().stream().map(
                        serviceProviderMapper::toServiceProviderSimpleDTO)
                        .collect(Collectors.toList()))).collect(Collectors.toList());
    }
    public FacilityDTO removeServiceProviderById(Long facilityId,Long serviceProviderId){
        var facility = facilityRepository.findById(facilityId).orElseThrow(()->new IllegalArgumentException("Facility Not Found !"));
        var serviceProvider = serviceProviderRepository.findById(serviceProviderId).orElseThrow(()->new IllegalArgumentException("Service Provider Not Found !"));
        facility.getServiceProviders().remove(serviceProvider);
        facilityRepository.save(facility);
        var serviceProviders = facility.getServiceProviders();
       var serviceProvidersDTO = serviceProviders.stream().map(serviceProviderMapper::toServiceProviderSimpleDTO).collect(Collectors.toList());
        return facilityMapper.toFacilityDTO(facility,serviceProvidersDTO);
    }
}
