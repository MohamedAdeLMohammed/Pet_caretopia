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
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<FacilitySimpleDTO> createFacility(@RequestBody FacilitySimpleDTO facility){
        return ResponseEntity.ok(facilityService.createFacility(facility));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/facility/{facilityId}")
    public ResponseEntity<FacilitySimpleDTO> updateFacility(@PathVariable Long facilityId , @RequestBody FacilitySimpleDTO facility){
        return ResponseEntity.ok(facilityService.updateFacility(facilityId,facility));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/facility/{facilityId}")
    public ResponseEntity<String> deleteFacility(@PathVariable Long facilityId){
        return ResponseEntity.ok(facilityService.deleteFacilityById(facilityId));
    }
}
