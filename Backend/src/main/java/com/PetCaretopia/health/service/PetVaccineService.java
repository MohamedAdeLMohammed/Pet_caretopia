package com.PetCaretopia.health.service;

import com.PetCaretopia.health.DTO.PetVaccineDTO;
import com.PetCaretopia.health.entity.PetVaccine;
import com.PetCaretopia.health.mapper.PetVaccineMapper;
import com.PetCaretopia.health.mapper.VaccineMapper;
import com.PetCaretopia.health.repository.PetVaccineRepository;
import com.PetCaretopia.health.repository.VaccineRepository;
import com.PetCaretopia.health.repository.VaccineTypeRepository;
import com.PetCaretopia.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.PetCaretopia.pet.Mapper.PetMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetVaccineService {
    private final PetRepository petRepository;
    private final PetMapper petMapper;
    private final PetVaccineRepository petVaccineRepository;
    private final PetVaccineMapper petVaccineMapper;
    private final VaccineRepository vaccineRepository;
    private final VaccineTypeRepository vaccineTypeRepository;
    private final VaccineMapper vaccineMapper;

    public PetVaccineDTO createPetVaccine(Long petId,PetVaccineDTO dto){
        var pet = petRepository.findById(petId).orElseThrow(()-> new IllegalArgumentException("Pet Not Found !"));
        Long vaccineId = dto.getPetVaccine().getVaccineId();
        var vaccine = vaccineRepository.findById(vaccineId).orElseThrow(()->new IllegalArgumentException("Vaccine not found !"));
        PetVaccine petVaccine = PetVaccine.builder()
                .pet(pet)
                .vaccine(vaccine)
                .notes(dto.getNotes())
                .createdAt(LocalDateTime.now())
                .vaccinationDate(dto.getVaccinationDate())
                .nextDoseDue(dto.getNextDoseDue())
                .updatedAt(LocalDateTime.now())
                .build();
        petVaccineRepository.save(petVaccine);
        return petVaccineMapper.toPetVaccineDTO(petVaccine);
    }
    public List<PetVaccineDTO> getAllPetVaccines(){
        var petVaccines = petVaccineRepository.findAll();
        return petVaccines.stream().map(petVaccineMapper::toPetVaccineDTO).collect(Collectors.toList());
    }
    public List<PetVaccineDTO> getAllPetVaccinesByVaccineName(String vaccineName){
        var vaccine = vaccineRepository.findByName(vaccineName).orElseThrow(()->new IllegalArgumentException("This Vaccine Not Found ! "+vaccineName));
        var petVaccines = petVaccineRepository.findByVaccine_Name(vaccineName);
        return petVaccines.stream().map(petVaccineMapper::toPetVaccineDTO).collect(Collectors.toList());
    }
    public List<PetVaccineDTO> getAllPetVaccinesByVaccineId(Long vaccineId){
        var vaccine = vaccineRepository.findById(vaccineId).orElseThrow(()->new IllegalArgumentException("This Vaccine Not Found ! "));
        var petVaccines = petVaccineRepository.findByVaccine_Id(vaccineId);
        return petVaccines.stream().map(petVaccineMapper::toPetVaccineDTO).collect(Collectors.toList());
    }
    public List<PetVaccineDTO> getAllPetVaccinesByVaccineTypeName(String vaccineTypeName){
        var vaccineType = vaccineTypeRepository.findByName(vaccineTypeName).orElseThrow(()->new IllegalArgumentException("This Type Not Found ! "+vaccineTypeName));
        var petVaccines = petVaccineRepository.findByVaccine_Type_Name(vaccineTypeName);
        return petVaccines.stream().map(petVaccineMapper::toPetVaccineDTO).collect(Collectors.toList());
    }
    public List<PetVaccineDTO> getAllPetVaccinesByVaccineTypeId(Long vaccineTypeId){
        var vaccineType = vaccineTypeRepository.findById(vaccineTypeId).orElseThrow(()->new IllegalArgumentException("This Type Not Found !"));
        var petVaccines = petVaccineRepository.findByVaccine_Type_Id(vaccineTypeId);
        return petVaccines.stream().map(petVaccineMapper::toPetVaccineDTO).collect(Collectors.toList());
    }
    public List<PetVaccineDTO> getAllPetVaccinesByPetId(Long petId){
        var pet = petRepository.findById(petId).orElseThrow(()->new IllegalArgumentException("Pet Not Found !"));
        var petVaccines = petVaccineRepository.findByPet_PetID(petId);
        return petVaccines.stream().map(petVaccineMapper::toPetVaccineDTO).collect(Collectors.toList());
    }
    public PetVaccineDTO updatePetVaccine(Long petId,Long petVaccineId,PetVaccineDTO dto){
        var existPetVaccine = petVaccineRepository.findById(petVaccineId).orElseThrow(()->new IllegalArgumentException("Pet Vaccines Not Found !"));
        var pet = petRepository.findById(petId).orElseThrow(()->new IllegalArgumentException("Pet Not Found !"));
        if(dto.getPetVaccine() != null){
            var vaccine = vaccineRepository.findById(dto.getPetVaccine().getVaccineId()).orElseThrow(()->new IllegalArgumentException("Vaccine not found !"));
            existPetVaccine.setVaccine(vaccine);
        }
        if(pet != null){
            existPetVaccine.setPet(pet);
        }
       if(dto.getVaccinationDate() != null){
           existPetVaccine.setVaccinationDate(dto.getVaccinationDate());
       }
       if(dto.getNextDoseDue() != null){
           existPetVaccine.setNextDoseDue(dto.getNextDoseDue());
       }
       if(dto.getNotes() != null){
           existPetVaccine.setNotes(dto.getNotes());
       }
       existPetVaccine.setUpdatedAt(LocalDateTime.now());
       petVaccineRepository.save(existPetVaccine);
       return petVaccineMapper.toPetVaccineDTO(existPetVaccine);
    }
    public List<PetVaccineDTO> removeVaccineFromPetByPetId(Long petId , Long vaccineId){
        var petVaccine = petVaccineRepository.findByPet_PetIDAndVaccine_Id(petId,vaccineId).orElseThrow(()->new IllegalArgumentException("Pet Vaccine Not Found !"));
        petVaccineRepository.delete(petVaccine);
        var petVaccines = petVaccineRepository.findByPet_PetID(petId);
        return petVaccines.stream().map(petVaccineMapper::toPetVaccineDTO).collect(Collectors.toList());
    }
    public String deleteAllPetVaccinesByPetId(Long petId){
        var petVaccines = petVaccineRepository.findByPet_PetID(petId);
        petVaccineRepository.deleteAll(petVaccines);
        return "Pet Vaccines Deleted !";
    }

}
