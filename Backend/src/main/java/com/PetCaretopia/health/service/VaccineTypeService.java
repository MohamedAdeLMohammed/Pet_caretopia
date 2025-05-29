package com.PetCaretopia.health.service;

import com.PetCaretopia.health.DTO.VaccineTypeDTO;
import com.PetCaretopia.health.entity.Vaccine;
import com.PetCaretopia.health.entity.VaccineType;
import com.PetCaretopia.health.mapper.VaccineTypeMapper;
import com.PetCaretopia.health.repository.VaccineRepository;
import com.PetCaretopia.health.repository.VaccineTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VaccineTypeService {
    private final VaccineTypeRepository vaccineTypeRepository;
    private final VaccineTypeMapper vaccineTypeMapper;
    private final VaccineRepository vaccineRepository;

    public VaccineTypeDTO createVaccineType(VaccineType type){
        type = VaccineType.builder()
                .name(type.getName())
                .description(type.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        vaccineTypeRepository.save(type);
        return vaccineTypeMapper.toVaccineTypeDTO(type);
    }
    public VaccineTypeDTO getVaccineTypeByName(String typeName){
        VaccineType type = vaccineTypeRepository.findByName(typeName).orElseThrow(()->new IllegalArgumentException("This Type is not found !"));
        return vaccineTypeMapper.toVaccineTypeDTO(type);
    }
    public VaccineTypeDTO getVaccineTypeById(Long id){
        VaccineType type = vaccineTypeRepository.findById(id).orElseThrow(()->new IllegalArgumentException("This Type is not found! "));
        return vaccineTypeMapper.toVaccineTypeDTO(type);
    }
    public String deleteVaccineTypeById(Long id){
        VaccineType type = vaccineTypeRepository.findById(id).orElseThrow(()->new IllegalArgumentException("This Type is not found! "));
        List<Vaccine> vaccines = vaccineRepository.findByType_Id(id);
        if(!vaccines.isEmpty()){
            throw new IllegalArgumentException("Can not delete this vaccine type because there are some vaccines relate to this type: "+type.getName());
        }
        vaccineTypeRepository.delete(type);
        return "Vaccine Type is Deleted !";
    }
    public VaccineTypeDTO updateVaccineType(Long id,VaccineType type){
        VaccineType existType = vaccineTypeRepository.findById(id).orElseThrow(()->new IllegalArgumentException("This Type is not found! "));
        if(type.getName() != null){
            existType.setName(type.getName());
        }
        if(type.getDescription() != null){
            existType.setDescription(type.getDescription());
        }
        existType.setUpdatedAt(LocalDateTime.now());
        vaccineTypeRepository.save(existType);
        return vaccineTypeMapper.toVaccineTypeDTO(existType);
    }
    public List<VaccineTypeDTO> getAllVaccineTypes(){
        List<VaccineType> vaccineTypes = vaccineTypeRepository.findAll();
        return vaccineTypes.stream().map(vaccineTypeMapper::toVaccineTypeDTO).collect(Collectors.toList());
    }
}
