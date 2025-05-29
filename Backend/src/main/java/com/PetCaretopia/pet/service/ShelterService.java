package com.PetCaretopia.pet.service;

import com.PetCaretopia.Security.Service.CustomUserDetails;
import com.PetCaretopia.pet.DTO.AdoptionDTO;
import com.PetCaretopia.pet.DTO.PetDTO;
import com.PetCaretopia.pet.DTO.ShelterDTO;
import com.PetCaretopia.pet.Mapper.AdoptionMapper;
import com.PetCaretopia.pet.Mapper.PetMapper;
import com.PetCaretopia.pet.Mapper.ShelterMapper;
import com.PetCaretopia.pet.entity.Pet;
import com.PetCaretopia.pet.entity.Shelter;
import com.PetCaretopia.pet.repository.AdoptionRepository;
import com.PetCaretopia.pet.repository.PetRepository;
import com.PetCaretopia.pet.repository.ShelterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShelterService {

    private final ShelterRepository shelterRepository;
    private final PetRepository petRepository;
    private final AdoptionRepository adoptionRepository;
    public List<ShelterDTO> getAll() {
        return shelterRepository.findAll().stream()
                .map(ShelterMapper::toDTO)
                .toList();
    }
    public ShelterDTO create(ShelterDTO dto, CustomUserDetails principal) {
        Shelter shelter = ShelterMapper.toEntity(dto);
        shelter.setCreatedBy(principal.getUserId()); // ✅ حل المشكلة
        return ShelterMapper.toDTO(shelterRepository.save(shelter));
    }


    public ShelterDTO getById(Long id) {
        Shelter shelter = shelterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shelter not found"));
        return ShelterMapper.toDTO(shelter);
    }

    public ShelterDTO update(Long id, ShelterDTO dto) {
        Shelter existing = shelterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shelter not found"));

        existing.setName(dto.getName());
        existing.setLocation(dto.getLocation());
        existing.setContactNumber(dto.getContactNumber());
        existing.setEmail(dto.getEmail());
        existing.setDescription(dto.getDescription());
        existing.setWebsiteUrl(dto.getWebsiteUrl());

        return ShelterMapper.toDTO(shelterRepository.save(existing));
    }

    public void delete(Long id) {
        Shelter shelter = shelterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shelter not found"));
        shelterRepository.delete(shelter);
    }

    public List<PetDTO> getPetsByShelter(Long shelterId) {
        Shelter shelter = shelterRepository.findById(shelterId)
                .orElseThrow(() -> new EntityNotFoundException("Shelter not found"));

        List<Pet> pets = petRepository.findByShelter(shelter);

        return pets.stream()
                .map(PetMapper::toDTO)
                .toList();
    }
    public List<AdoptionDTO> getAdoptionsByShelter(Long shelterId) {
        return adoptionRepository.findByShelterId(shelterId).stream()
                .map(AdoptionMapper::toDTO)
                .toList();
    }


}
