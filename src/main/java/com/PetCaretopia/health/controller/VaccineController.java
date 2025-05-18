package com.PetCaretopia.health.controller;

import com.PetCaretopia.health.DTO.VaccineDTO;
import com.PetCaretopia.health.entity.Vaccine;
import com.PetCaretopia.health.mapper.VaccineMapper;
import com.PetCaretopia.health.mapper.VaccineTypeMapper;
import com.PetCaretopia.health.service.VaccineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vaccines")
@RequiredArgsConstructor
public class VaccineController {
    private final VaccineService vaccineService;
    private final VaccineMapper vaccineMapper;
    private final VaccineTypeMapper vaccineTypeMapper;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<VaccineDTO> createVaccine(@RequestBody VaccineDTO vaccine){
        return ResponseEntity.ok(vaccineService.createVaccine(vaccine));
    }
    @GetMapping("/type")
    public ResponseEntity<List<VaccineDTO>> getVaccinesByType(@RequestParam String vaccineTypeName){
        return ResponseEntity.ok(vaccineService.getAllVaccinesByType(vaccineTypeName));
    }
    @GetMapping("/vaccine/{id}")
    public ResponseEntity<VaccineDTO> getVaccineById(@PathVariable Long id){
        return ResponseEntity.ok(vaccineService.getVaccineById(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/vaccine/{id}")
    public ResponseEntity<VaccineDTO> updateVaccineById(@PathVariable Long id , @RequestBody VaccineDTO type){
        return ResponseEntity.ok(vaccineService.updateVaccine(id,type));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/vaccine/{id}")
    public ResponseEntity<String> deleteVaccineById(@PathVariable Long id){
        return ResponseEntity.ok(vaccineService.deleteVaccineById(id));
    }
    @GetMapping("/all")
    public ResponseEntity<List<VaccineDTO>> getAllVaccines(){
        return ResponseEntity.ok(vaccineService.getAllVaccines());
    }
}
