package com.PetCaretopia.user.mapper;

import com.PetCaretopia.Security.Util.GetAgeUtil;
import com.PetCaretopia.facility.mapper.FacilityMapper;
import com.PetCaretopia.user.DTO.ServiceProviderDTO;
import com.PetCaretopia.user.DTO.ServiceProviderSimpleDTO;
import com.PetCaretopia.user.entity.ServiceProvider;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ServiceProviderMapper {
    private final GetAgeUtil getAgeUtil;
    private final FacilityMapper facilityMapper;

    public ServiceProviderMapper(GetAgeUtil getAgeUtil, FacilityMapper facilityMapper) {
        this.getAgeUtil = getAgeUtil;
        this.facilityMapper = facilityMapper;
    }
    public ServiceProviderDTO toServiceProviderDTO(ServiceProvider serviceProvider){
        return new ServiceProviderDTO(
                serviceProvider.getServiceProviderID(),
                serviceProvider.getUser().getName(),
                serviceProvider.getUser().getUserEmail(),
                serviceProvider.getUser().getUserPhoneNumber(),
                serviceProvider.getUser().getUserAddress(),
                serviceProvider.getUser().getUserGender(),
                serviceProvider.getUser().getUserStatus(),
                getAgeUtil.getAge(serviceProvider.getUser().getBirthDate()),
                serviceProvider.getUser().getBirthDate(),
                serviceProvider.getUser().getUserDetails(),
                serviceProvider.getUser().getUserProfileImage(),
                serviceProvider.getServiceProviderSalary(),
                serviceProvider.getServiceProviderType(),
                serviceProvider.getServiceProviderExperience(),
                serviceProvider.getFacilities().stream().map(facilityMapper::toFacilitySimpleDTO).collect(Collectors.toList())
        );
    }
    public ServiceProviderSimpleDTO toServiceProviderSimpleDTO(ServiceProvider serviceProvider){
        return new ServiceProviderSimpleDTO(
                serviceProvider.getServiceProviderID(),
                serviceProvider.getUser().getName(),
                serviceProvider.getUser().getUserEmail(),
                serviceProvider.getUser().getUserPhoneNumber(),
                serviceProvider.getUser().getUserAddress(),
                serviceProvider.getUser().getUserGender(),
                serviceProvider.getUser().getUserStatus(),
                getAgeUtil.getAge(serviceProvider.getUser().getBirthDate()),
                serviceProvider.getUser().getBirthDate(),
                serviceProvider.getUser().getUserDetails(),
                serviceProvider.getUser().getUserProfileImage(),
                serviceProvider.getServiceProviderSalary(),
                serviceProvider.getServiceProviderType(),
                serviceProvider.getServiceProviderExperience()

        );
    }
}
