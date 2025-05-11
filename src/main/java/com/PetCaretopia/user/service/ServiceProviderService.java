package com.PetCaretopia.user.service;



import com.PetCaretopia.user.entity.ServiceProvider;
import com.PetCaretopia.user.repository.ServiceProviderRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceProviderService {

    private final ServiceProviderRepository serviceProviderRepository;

    public ServiceProviderService(ServiceProviderRepository serviceProviderRepository) {
        this.serviceProviderRepository = serviceProviderRepository;
    }

    public ServiceProvider saveServiceProvider(ServiceProvider serviceProvider) {
        return serviceProviderRepository.save(serviceProvider);
    }

    public Optional<ServiceProvider> getServiceProviderByUserId(Long userID) {
        return serviceProviderRepository.findByUser_UserID(userID);
    }

    public List<ServiceProvider> getProvidersByType(String type) {
        return serviceProviderRepository.findByServiceProviderType(type);
    }
}
