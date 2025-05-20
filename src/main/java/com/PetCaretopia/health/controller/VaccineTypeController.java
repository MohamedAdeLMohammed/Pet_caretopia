package com.PetCaretopia.health.controller;

import com.PetCaretopia.health.DTO.VaccineTypeDTO;
import com.PetCaretopia.health.entity.VaccineType;
import com.PetCaretopia.health.service.VaccineTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("/vaccineTypes")
@RequiredArgsConstructor
public class VaccineTypeController {
    private final VaccineTypeService vaccineTypeService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<VaccineTypeDTO> createNewVaccineType(@RequestBody VaccineType vaccineType){
        return ResponseEntity.ok(vaccineTypeService.createVaccineType(vaccineType));
    }
    @GetMapping("/vaccineType/type")
    public ResponseEntity<VaccineTypeDTO> getVaccineTypeByType(@RequestParam String type){
        return ResponseEntity.ok(vaccineTypeService.getVaccineTypeByName(type));
    }
    @GetMapping("/vaccineType/{id}")
    public ResponseEntity<VaccineTypeDTO> getVaccineTypeById(@PathVariable Long id){
        return ResponseEntity.ok(vaccineTypeService.getVaccineTypeById(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/vaccineType/{id}")
    public ResponseEntity<VaccineTypeDTO> updateVaccineType(@PathVariable Long id,@RequestBody VaccineType type){
        return ResponseEntity.ok(vaccineTypeService.updateVaccineType(id,type));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/vaccineType/{id}")
    public ResponseEntity<String> deleteVaccineType(@PathVariable Long id){
        return ResponseEntity.ok(vaccineTypeService.deleteVaccineTypeById(id));
    }
    @GetMapping("/all")
    public ResponseEntity<List<VaccineTypeDTO>> getAllVaccineTypes(){
        return ResponseEntity.ok(vaccineTypeService.getAllVaccineTypes());
    }
}
