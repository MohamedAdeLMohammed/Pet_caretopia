package com.PetCaretopia.health.controller;

import com.PetCaretopia.health.DTO.PetVaccineDTO;
import com.PetCaretopia.health.service.PetVaccineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/petVaccines")
@RequiredArgsConstructor
public class PetVaccineController {
    private final PetVaccineService petVaccineService;
    @GetMapping("/all")
    public ResponseEntity<List<PetVaccineDTO>> getAllPetVaccines(){
        return ResponseEntity.ok(petVaccineService.getAllPetVaccines());
    }
    @GetMapping("/vaccine")
    public ResponseEntity<List<PetVaccineDTO>> getAllPetVaccinesByVaccineName(@RequestParam String name){
        return ResponseEntity.ok(petVaccineService.getAllPetVaccinesByVaccineName(name));
    }
    @GetMapping("/vaccine/{vaccineId}")
    public ResponseEntity<List<PetVaccineDTO>> getAllPetVaccinesByVaccineId(@PathVariable Long vaccineId){
        return ResponseEntity.ok(petVaccineService.getAllPetVaccinesByVaccineId(vaccineId));
    }
    @GetMapping("/vaccineType")
    public ResponseEntity<List<PetVaccineDTO>> getAllPetVaccinesByVaccineTypeName(@RequestParam String name){
        return ResponseEntity.ok(petVaccineService.getAllPetVaccinesByVaccineTypeName(name));
    }
    @GetMapping("/vaccineType/{vaccineTypeId}")
    public ResponseEntity<List<PetVaccineDTO>> getAllPetVaccinesByVaccineTypeId(@PathVariable Long vaccineTypeId){
        return ResponseEntity.ok(petVaccineService.getAllPetVaccinesByVaccineTypeId(vaccineTypeId));
    }
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<PetVaccineDTO>> getAllPetVaccinesByPetId(@PathVariable Long petId){
        return ResponseEntity.ok(petVaccineService.getAllPetVaccinesByPetId(petId));
    }
    @PostMapping("/add/pet/{petId}")
    public ResponseEntity<PetVaccineDTO> createPetVaccine(@PathVariable Long petId,@RequestBody PetVaccineDTO dto){
        return ResponseEntity.ok(petVaccineService.createPetVaccine(petId,dto));
    }
    @PutMapping("/pet/{petId}/vaccine/{petVaccineId}")
    public ResponseEntity<PetVaccineDTO> updatePetVaccine(@PathVariable Long petId ,@PathVariable Long petVaccineId ,@RequestBody PetVaccineDTO dto){
        return ResponseEntity.ok(petVaccineService.updatePetVaccine(petId,petVaccineId,dto));
    }
    @DeleteMapping("/pet/{petId}/vaccine/{vaccineId}")
    public ResponseEntity<List<PetVaccineDTO>> removeVaccineFromPet(@PathVariable Long petId , @PathVariable Long vaccineId){
        return ResponseEntity.ok(petVaccineService.removeVaccineFromPetByPetId(petId,vaccineId));
    }
    @DeleteMapping("/pet/{petId}")
    public ResponseEntity<String> removeAllPetVaccines(@PathVariable Long petId){
        return ResponseEntity.ok(petVaccineService.deleteAllPetVaccinesByPetId(petId));
    }
}
