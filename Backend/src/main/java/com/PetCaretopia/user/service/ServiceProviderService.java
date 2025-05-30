package com.PetCaretopia.user.service;



import com.PetCaretopia.Chat.Repository.AccountRepository;
import com.PetCaretopia.facility.entity.Appointment;
import com.PetCaretopia.facility.entity.AppointmentRequest;
import com.PetCaretopia.facility.entity.Facility;
import com.PetCaretopia.facility.mapper.FacilityMapper;
import com.PetCaretopia.facility.repository.AppointmentRepository;
import com.PetCaretopia.facility.repository.AppointmentRequestRepository;
import com.PetCaretopia.facility.repository.FacilityRepository;
import com.PetCaretopia.shared.SharedImageUploadService;
import com.PetCaretopia.user.DTO.ServiceProviderDTO;
import com.PetCaretopia.user.entity.ServiceProvider;
import com.PetCaretopia.user.Mapper.ServiceProviderMapper;
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

    private final AccountRepository accountRepository;
    private final UserService userService;
    private final ServiceProviderMapper serviceProviderMapper;
    private final SharedImageUploadService imageUploadService;
    private final FacilityRepository facilityRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentRequestRepository appointmentRequestRepository;
    public ServiceProviderService(ServiceProviderRepository serviceProviderRepository, AccountRepository accountRepository, UserService userService, ServiceProviderMapper serviceProviderMapper, SharedImageUploadService imageUploadService, FacilityRepository facilityRepository, AppointmentRepository appointmentRepository, AppointmentRequestRepository appointmentRequestRepository) {
        this.serviceProviderRepository = serviceProviderRepository;
        this.accountRepository = accountRepository;
        this.userService = userService;
        this.serviceProviderMapper = serviceProviderMapper;
        this.imageUploadService = imageUploadService;
        this.facilityRepository = facilityRepository;
        this.appointmentRepository = appointmentRepository;
        this.appointmentRequestRepository = appointmentRequestRepository;
    }


    public String deleteServiceProvider(Long userId){
       ServiceProvider serviceProvider = serviceProviderRepository.findByUser_UserID(userId).orElseThrow(()-> new IllegalArgumentException("Service Provider Not Found !"));
       var account = accountRepository.findByUsername(serviceProvider.getUser().getUserEmail()).orElseThrow(()->new IllegalArgumentException("Account  Not Found !"));
        var requests = appointmentRequestRepository.findByServiceProvider_ServiceProviderID(serviceProvider.getServiceProviderID());
        var appointments = appointmentRepository.findByServiceProvider_ServiceProviderID(serviceProvider.getServiceProviderID());

        boolean hasPendingRequests = requests != null && requests.stream()
                .anyMatch(request -> request.getStatus() == AppointmentRequest.AppointmentRequestStatus.PENDING);

        boolean hasNotTreatedAppointments = appointments != null && appointments.stream()
                .anyMatch(appointment -> appointment.getAppointmentStatus() == Appointment.AppointmentStatus.NOT_TREATED);

        if (hasPendingRequests || hasNotTreatedAppointments) {
            throw new IllegalArgumentException("You cannot delete this service provider because there are some Requests and Appointments still not handled!");
        }

        if (appointments != null) {
            appointments.forEach(appointmentRepository::delete);
        }

        if (requests != null) {
            requests.forEach(appointmentRequestRepository::delete);
        }
       accountRepository.delete(account);
       serviceProviderRepository.delete(serviceProvider);
       return "Deleted !";
    }
    @Transactional
    public ServiceProviderDTO updateServiceProvider(Long userId, ServiceProviderDTO dto, MultipartFile image){
        var existingServiceProvider = serviceProviderRepository.findByUser_UserID(userId).orElseThrow(()->new IllegalArgumentException("I can Not Found service provider with userId : !"+userId));
        var user = existingServiceProvider.getUser();
        if(dto != null){
            if(dto.getName() != null){
                user.setName(dto.getName());
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
            if(dto.getServiceProviderType() != null){
                if(existingServiceProvider.getFacilities() != null){
                    throw new IllegalArgumentException("You Cannot Change Your Service Provider Type Because it's related to your facilities so you must delete these facilities first !");
                }
                existingServiceProvider.setServiceProviderType(dto.getServiceProviderType());
            }
            if(dto.getServiceProviderExperience() != null){
                existingServiceProvider.setServiceProviderExperience(dto.getServiceProviderExperience());
            }
            if(dto.getServiceProviderSalary() != null){
                existingServiceProvider.setServiceProviderSalary(dto.getServiceProviderSalary());
            }
        }
        if(image != null && !image.isEmpty()){
            String imageUrl = imageUploadService.uploadMultipartFile(image);
            user.setUserProfileImage(imageUrl);
        }
        else {
            user.setUserProfileImage(user.getUserProfileImage());
        }

        var facilities = facilityRepository.findFacilitiesByServiceProvider_ServiceProviderID(existingServiceProvider.getServiceProviderID());
        if(facilities != null){
            existingServiceProvider.getFacilities().clear();
            existingServiceProvider.getFacilities().addAll(facilities);
        }

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

}
