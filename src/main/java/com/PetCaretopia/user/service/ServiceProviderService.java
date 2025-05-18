package com.PetCaretopia.user.service;



import com.PetCaretopia.Chat.Repository.AccountRepository;
import com.PetCaretopia.facility.entity.Facility;
import com.PetCaretopia.facility.mapper.FacilityMapper;
import com.PetCaretopia.facility.repository.FacilityRepository;
import com.PetCaretopia.shared.SharedImageUploadService;
import com.PetCaretopia.user.DTO.ServiceProviderDTO;
import com.PetCaretopia.user.DTO.ServiceProviderSimpleDTO;
import com.PetCaretopia.user.entity.ServiceProvider;
import com.PetCaretopia.user.mapper.ServiceProviderMapper;
import com.PetCaretopia.user.repository.ServiceProviderRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceProviderService {

    private final ServiceProviderRepository serviceProviderRepository;

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final ServiceProviderMapper serviceProviderMapper;
    private final SharedImageUploadService imageUploadService;
    private final FacilityMapper facilityMapper;
    private final FacilityRepository facilityRepository;
    public ServiceProviderService(ServiceProviderRepository serviceProviderRepository, PasswordEncoder passwordEncoder, AccountRepository accountRepository, UserService userService, ServiceProviderMapper serviceProviderMapper, SharedImageUploadService imageUploadService, FacilityMapper facilityMapper, FacilityRepository facilityRepository) {
        this.serviceProviderRepository = serviceProviderRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.userService = userService;
        this.serviceProviderMapper = serviceProviderMapper;
        this.imageUploadService = imageUploadService;
        this.facilityMapper = facilityMapper;
        this.facilityRepository = facilityRepository;
    }


    public String deleteServiceProvider(Long userId){
       ServiceProvider serviceProvider = serviceProviderRepository.findByUser_UserID(userId).orElseThrow(()-> new IllegalArgumentException("Service Provider Not Found !"));
       var account = accountRepository.findByUsername(serviceProvider.getUser().getUserEmail()).orElseThrow(()->new IllegalArgumentException("Account  Not Found !"));
       accountRepository.delete(account);
       serviceProviderRepository.delete(serviceProvider);
       return "Deleted !";
    }
    @Transactional
    public ServiceProviderDTO updateServiceProvider(Long userId, ServiceProviderDTO dto, MultipartFile image){
        var existingServiceProvider = serviceProviderRepository.findByUser_UserID(userId).orElseThrow(()->new IllegalArgumentException("I can Not Found service provider with userId : !"+userId));
        var account = accountRepository.findByUsername(existingServiceProvider.getUser().getUserEmail()).orElseThrow(()->new IllegalArgumentException("Not Found !"));
        var user = existingServiceProvider.getUser();

        if(dto.getName() != null){
            user.setName(dto.getName());
        }

        if(dto.getUserPhoneNumber() != null){
            user.setUserPhoneNumber(dto.getUserPhoneNumber());
            account.setPassword(passwordEncoder.encode(dto.getUserPhoneNumber()));
        }
        if(dto.getUserGender() != null){
            user.setUserGender(dto.getUserGender());
        }
        if(dto.getUserAddress() != null){
            user.setUserAddress(dto.getUserAddress());
        }
        if(dto.getBirthDate() != null){
            user.setBirthDate(dto.getBirthDate());
        }
        if(dto.getUserDetails() != null){
            user.setUserDetails(dto.getUserDetails());
        }
        if(dto.getUserStatus() != null){
            user.setUserStatus(dto.getUserStatus());
        }
        if(image != null && !image.isEmpty()){
            String imageUrl = imageUploadService.uploadMultipartFile(image);
            user.setUserProfileImage(imageUrl);
        }
        else {
            user.setUserProfileImage(user.getUserProfileImage());
        }
        if(dto.getServiceProviderType() != null){
            existingServiceProvider.setServiceProviderType(dto.getServiceProviderType());
        }
        if(dto.getServiceProviderExperience() != null){
            existingServiceProvider.setServiceProviderExperience(dto.getServiceProviderExperience());
        }
        if(dto.getServiceProviderSalary() != null){
            existingServiceProvider.setServiceProviderSalary(dto.getServiceProviderSalary());
        }
        if (dto.getFacilities() != null) {
            List<Facility> managedFacilities = dto.getFacilities().stream()
                    .map(fDto -> facilityRepository.findById(fDto.getFacilityId())
                            .orElseThrow(() -> new IllegalArgumentException("Facility not found: " + fDto.getFacilityId())))
                    .collect(Collectors.toList());

            existingServiceProvider.setFacilities(managedFacilities);
        }
        accountRepository.save(account);
        serviceProviderRepository.save(existingServiceProvider);
        userService.saveUser(user);
        return serviceProviderMapper.toServiceProviderDTO(existingServiceProvider);
    }

    public ServiceProviderDTO getServiceProviderByUserId(Long userID) {
        ServiceProvider serviceProvider = serviceProviderRepository.findByUser_UserID(userID).orElseThrow(()->new IllegalArgumentException("Service Provider not found !"));
         return serviceProviderMapper.toServiceProviderDTO(serviceProvider);
    }

    public List<ServiceProviderDTO> getProvidersByType(ServiceProvider.ServiceProviderType type) {
        List<ServiceProvider> serviceProviders = serviceProviderRepository.findByServiceProviderType(type);
        return serviceProviders.stream().map(serviceProviderMapper::toServiceProviderDTO).collect(Collectors.toList());
    }
    public ServiceProviderDTO getServiceProviderById(Long serviceProviderId){
        var serviceProvider = serviceProviderRepository.findById(serviceProviderId).orElseThrow(()->new IllegalArgumentException("Service Provider Not Found !"));
        return serviceProviderMapper.toServiceProviderDTO(serviceProvider);
    }
    public List<ServiceProviderDTO> getAllServiceProvidersWithFacilities(){
        var serviceProviders = serviceProviderRepository.findAll();
        return serviceProviders.stream().map(
                serviceProviderMapper::toServiceProviderDTO
        ).collect(Collectors.toList());
    }
    public ServiceProviderDTO removeFacilityById(Long userId,Long facilityId){
        var serviceProvider = serviceProviderRepository.findByUser_UserID(userId).orElseThrow(()->new IllegalArgumentException("Service Provider Not Found !"));
        var facility = facilityRepository.findById(facilityId).orElseThrow(()->new IllegalArgumentException("Facility Not Found !"));
        serviceProvider.getFacilities().remove(facility);
        serviceProviderRepository.save(serviceProvider);
        return serviceProviderMapper.toServiceProviderDTO(serviceProvider);
    }
}
