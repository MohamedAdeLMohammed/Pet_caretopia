package  com.PetCaretopia.user.controller;


import com.PetCaretopia.Security.Service.CustomUserDetails;
import com.PetCaretopia.user.DTO.ServiceProviderDTO;
import com.PetCaretopia.user.DTO.ServiceProviderSimpleDTO;
import com.PetCaretopia.user.entity.ServiceProvider;
import com.PetCaretopia.user.service.ServiceProviderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/serviceProviders")
public class ServiceProviderController {

    private final ServiceProviderService serviceProviderService;

    public ServiceProviderController(ServiceProviderService serviceProviderService) {
        this.serviceProviderService = serviceProviderService;
    }


    @GetMapping("/user/{userID}")
    public ResponseEntity<ServiceProviderDTO> getServiceProviderByUserId(@PathVariable Long userID) {
        return ResponseEntity.ok(serviceProviderService.getServiceProviderByUserId(userID));
    }

    @GetMapping("/type/{serviceProviderType}")
    public ResponseEntity<List<ServiceProviderDTO>> getServiceProvidersByType(@RequestParam ServiceProvider.ServiceProviderType serviceProviderType) {
        return ResponseEntity.ok(serviceProviderService.getProvidersByType(serviceProviderType));
    }
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    @PutMapping("/user/{userId}")
    public ResponseEntity<?> updateServiceProvider(@PathVariable Long userId, @RequestPart("user") ServiceProviderDTO dto,@RequestPart(value = "image", required = false) MultipartFile image,@AuthenticationPrincipal CustomUserDetails userDetails){
        Long authId = userDetails.getUserId();
        if(!authId.equals(userId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(serviceProviderService.updateServiceProvider(userId,dto,image));
    }
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteServiceProvider(@PathVariable Long userId,@AuthenticationPrincipal CustomUserDetails userDetails){
        Long authId = userDetails.getUserId();
        if(!authId.equals(userId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(serviceProviderService.deleteServiceProvider(userId));
    }
    @GetMapping("/serviceProvider/{serviceProviderId}")
    public ResponseEntity<ServiceProviderDTO> getServiceProviderById(@PathVariable Long serviceProviderId){
        return ResponseEntity.ok(serviceProviderService.getServiceProviderById(serviceProviderId));
    }
    @GetMapping("/all")
    public ResponseEntity<List<ServiceProviderDTO>> getAllServiceProvidersWithFacilities(){
        return ResponseEntity.ok(serviceProviderService.getAllServiceProvidersWithFacilities());
    }
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    @DeleteMapping("/serviceProvider/{userId}/facility/{facilityId}")
    public ResponseEntity<ServiceProviderDTO> removeFacility(@PathVariable Long userId,@PathVariable Long facilityId,@AuthenticationPrincipal CustomUserDetails userDetails){
        Long authId = userDetails.getUserId();
        if(!authId.equals(userId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(serviceProviderService.removeFacilityById(userId,facilityId));
    }

}
