package com.PetCaretopia.pet.service;

import com.PetCaretopia.pet.DTO.PetBreedDTO;
import com.PetCaretopia.pet.Mapper.PetBreedMapper;
import com.PetCaretopia.pet.entity.PetBreed;
import com.PetCaretopia.pet.entity.PetType;
import com.PetCaretopia.pet.repository.PetBreedRepository;
import com.PetCaretopia.pet.repository.PetTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetBreedService {

    private final PetBreedRepository breedRepo;
    private final PetTypeRepository typeRepo;

    public List<PetBreedDTO> getAll() {
        return breedRepo.findAll().stream().map(PetBreedMapper::toDTO).toList();
    }

    public PetBreedDTO getById(Long id) {
        PetBreed breed = breedRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Breed not found"));
        return PetBreedMapper.toDTO(breed);
    }

    public List<PetBreedDTO> getByType(String typeName) {
        PetType type = typeRepo.findByTypeName(typeName)
                .orElseThrow(() -> new EntityNotFoundException("Invalid type"));
        return breedRepo.findByPetType(type).stream()
                .map(PetBreedMapper::toDTO)
                .toList();
    }

    public PetBreedDTO create(PetBreedDTO dto) {
        PetType type = typeRepo.findByTypeName(dto.getPetTypeName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid pet type"));
        PetBreed breed = PetBreedMapper.toEntity(dto, type);
        return PetBreedMapper.toDTO(breedRepo.save(breed));
    }

    public PetBreedDTO update(Long id, PetBreedDTO dto) {
        PetBreed existing = breedRepo.findById(id).orElseThrow();
        PetType type = typeRepo.findByTypeName(dto.getPetTypeName()).orElseThrow();
        existing.setBreedName(dto.getBreedName());
        existing.setPetType(type);
        return PetBreedMapper.toDTO(breedRepo.save(existing));
    }

    public void delete(Long id) {
        PetBreed breed = breedRepo.findById(id).orElseThrow();
        breedRepo.delete(breed);
    }
}
