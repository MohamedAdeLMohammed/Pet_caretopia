package  com.PetCaretopia.user.controller;


import com.PetCaretopia.user.entity.ServiceProvider;
import com.PetCaretopia.user.service.ServiceProviderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/serviceProviders")
public class ServiceProviderController {

    private final ServiceProviderService serviceProviderService;

    public ServiceProviderController(ServiceProviderService serviceProviderService) {
        this.serviceProviderService = serviceProviderService;
    }

    @PostMapping("/register")
    public ServiceProvider registerServiceProvider(@RequestBody ServiceProvider serviceProvider) {
        return serviceProviderService.saveServiceProvider(serviceProvider);
    }

    @GetMapping("/user/{userID}")
    public Optional<ServiceProvider> getServiceProviderByUserId(@PathVariable Long userID) {
        return serviceProviderService.getServiceProviderByUserId(userID);
    }

    @GetMapping("/type/{serviceProviderType}")
    public List<ServiceProvider> getServiceProvidersByType(@PathVariable String serviceProviderType) {
        return serviceProviderService.getProvidersByType(serviceProviderType);
    }
}
