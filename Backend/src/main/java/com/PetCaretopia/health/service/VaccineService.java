package com.PetCaretopia.health.service;

import com.PetCaretopia.health.DTO.VaccineDTO;
import com.PetCaretopia.health.entity.Vaccine;
import com.PetCaretopia.health.entity.VaccineType;
import com.PetCaretopia.health.mapper.VaccineMapper;
import com.PetCaretopia.health.mapper.VaccineTypeMapper;
import com.PetCaretopia.health.repository.PetVaccineRepository;
import com.PetCaretopia.health.repository.VaccineRepository;
import com.PetCaretopia.health.repository.VaccineTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VaccineService {
    private final VaccineRepository vaccineRepository;
    private final VaccineTypeRepository vaccineTypeRepository;
    private final VaccineMapper vaccineMapper;
    private final PetVaccineRepository petVaccineRepository;
    public List<VaccineDTO> getAllVaccinesByType(String vaccineType){
        VaccineType type = vaccineTypeRepository.findByName(vaccineType).orElseThrow(()->new IllegalArgumentException("This vaccine type : "+vaccineType+" is not found !"));
        List<Vaccine> vaccines = vaccineRepository.findByType_Id(type.getId());
        return vaccines.stream().map(vaccine -> vaccineMapper.toVaccineDTO(vaccine,type)).collect(Collectors.toList());
    }
    public VaccineDTO createVaccine(VaccineDTO vaccine){
        Long typeId = vaccine.getVaccineType().getId();
        VaccineType vaccineType = vaccineTypeRepository.findById(typeId).orElseThrow(()->new IllegalArgumentException("This Vaccine Type is not found !"));
         Vaccine newVaccine = Vaccine.builder()
                .name(vaccine.getVaccineName())
                .description(vaccine.getDescription())
                .recommendedAgeWeeks(vaccine.getRecommendedAgeWeeks())
                .type(vaccineType)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
         vaccineRepository.save(newVaccine);
         return vaccineMapper.toVaccineDTO(newVaccine,newVaccine.getType());
    }
    public VaccineDTO getVaccineById(Long id){
        Vaccine vaccine = vaccineRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Vaccine Not Found !"));
        return vaccineMapper.toVaccineDTO(vaccine,vaccine.getType());
    }
    public String deleteVaccineById(Long id){
        Vaccine vaccine = vaccineRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Vaccine Not Found !"));
        var petVaccines = petVaccineRepository.findByVaccine_Id(id);
        if(!petVaccines.isEmpty()){
            throw new IllegalArgumentException("Can not delete this vaccine because there are some pets use this vaccine !");
        }
        vaccineRepository.delete(vaccine);
        return "Vaccine Deleted !";
    }
    public VaccineDTO updateVaccine(Long id,VaccineDTO dto){
        Vaccine existVaccine = vaccineRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Vaccine Not Found !"));
        if(dto.getVaccineName() != null){
            existVaccine.setName(dto.getVaccineName());
        }
        if(dto.getDescription() != null){
            existVaccine.setDescription(dto.getDescription());
        }
        if(dto.getRecommendedAgeWeeks() != null){
            existVaccine.setRecommendedAgeWeeks(dto.getRecommendedAgeWeeks());
        }
        existVaccine.setUpdatedAt(LocalDateTime.now());
        vaccineRepository.save(existVaccine);
        return vaccineMapper.toVaccineDTO(existVaccine,existVaccine.getType());
    }
    public List<VaccineDTO> getAllVaccines(){
        List<Vaccine> vaccines = vaccineRepository.findAll();
        return vaccines.stream().map(vaccine -> vaccineMapper.toVaccineDTO(vaccine,vaccine.getType())).collect(Collectors.toList());
    }
}
