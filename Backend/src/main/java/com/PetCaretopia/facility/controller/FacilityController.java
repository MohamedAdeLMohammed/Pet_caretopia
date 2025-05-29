package com.PetCaretopia.facility.controller;

import com.PetCaretopia.facility.DTO.FacilityDTO;
import com.PetCaretopia.facility.DTO.FacilitySimpleDTO;
import com.PetCaretopia.facility.entity.Facility;
import com.PetCaretopia.facility.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facilities")
@RequiredArgsConstructor
public class FacilityController {
    private final FacilityService facilityService;

    @GetMapping("/all")
    public ResponseEntity<List<FacilityDTO>> getAllFacilitiesWithServiceProviders(){
        return ResponseEntity.ok(facilityService.getAllFacilitiesWithServiceProviders());
    }
    @GetMapping("/serviceProvider/{serviceProviderId}")
    public ResponseEntity<List<FacilitySimpleDTO>> getAllServiceProviderFacilitiesWithServiceProviderId(@PathVariable Long serviceProviderId){
        return ResponseEntity.ok(facilityService.getServiceProviderFacilitiesByServiceProviderId(serviceProviderId));
    }
    @GetMapping("/type")
    public ResponseEntity<List<FacilitySimpleDTO>> getFacilitiesByType(@RequestParam Facility.FacilityType type){
        return ResponseEntity.ok(facilityService.getFacilitiesByFacilityType(type));
    }
    @GetMapping("/facilityName")
    public ResponseEntity<FacilityDTO> getFacilityByName(@RequestParam String facilityName){
        return ResponseEntity.ok(facilityService.getFacilityWithServiceProvidersByFacilityName(facilityName));
    }
    @GetMapping("/facilityWithSP/{facilityId}")
    public ResponseEntity<FacilityDTO> getFacilityByFacilityId(@PathVariable Long facilityId){
        return ResponseEntity.ok(facilityService.getFacilityWithServiceProvidersByFacilityId(facilityId));
    }
    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<FacilitySimpleDTO> getFacilityById(@PathVariable Long facilityId){
        return ResponseEntity.ok(facilityService.getFacilityById(facilityId));
    }
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    @PostMapping("/add/toServiceProvider/{serviceProviderId}")
    public ResponseEntity<FacilitySimpleDTO> createFacility(@PathVariable Long serviceProviderId,@RequestBody FacilitySimpleDTO facility){
        return ResponseEntity.ok(facilityService.createFacility(serviceProviderId,facility));
    }
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    @PutMapping("/facility/{facilityId}")
    public ResponseEntity<FacilitySimpleDTO> updateFacility(@PathVariable Long facilityId , @RequestBody FacilitySimpleDTO facility){
        return ResponseEntity.ok(facilityService.updateFacility(facilityId,facility));
    }
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    @DeleteMapping("/facility/{facilityId}")
    public ResponseEntity<String> deleteFacility(@PathVariable Long facilityId){
        return ResponseEntity.ok(facilityService.deleteFacilityById(facilityId));
    }
}
