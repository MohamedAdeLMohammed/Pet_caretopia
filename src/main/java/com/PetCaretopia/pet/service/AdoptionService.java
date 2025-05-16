package com.PetCaretopia.pet.service;

import com.PetCaretopia.pet.DTO.AdoptionDTO;
import com.PetCaretopia.pet.Mapper.AdoptionMapper;
import com.PetCaretopia.pet.entity.Adoption;
import com.PetCaretopia.pet.entity.Pet;
import com.PetCaretopia.pet.entity.Shelter;
import com.PetCaretopia.pet.repository.AdoptionRepository;
import com.PetCaretopia.pet.repository.PetRepository;
import com.PetCaretopia.pet.repository.ShelterRepository;
import com.PetCaretopia.user.entity.PetOwner;
import com.PetCaretopia.user.repository.PetOwnerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final PetRepository petRepository;
    private final PetOwnerRepository petOwnerRepository;
    private final ShelterRepository shelterRepository;

    public AdoptionDTO submitRequest(AdoptionDTO dto) {
        Pet pet = petRepository.findById(dto.getPetId()).orElseThrow();
        PetOwner adopter = petOwnerRepository.findById(dto.getAdopterId()).orElseThrow();
        PetOwner previous = dto.getPreviousOwnerId() != null ? petOwnerRepository.findById(dto.getPreviousOwnerId()).orElse(null) : null;
        Shelter shelter = dto.getShelterId() != null ? shelterRepository.findById(dto.getShelterId()).orElse(null) : null;

        Adoption adoption = AdoptionMapper.toEntity(dto, pet, adopter, previous, shelter);
        return AdoptionMapper.toDTO(adoptionRepository.save(adoption));
    }

    public List<AdoptionDTO> getAll() {
        return adoptionRepository.findAll().stream()
                .map(AdoptionMapper::toDTO)
                .toList();
    }

    public AdoptionDTO getById(Long id) {
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adoption not found"));
        return AdoptionMapper.toDTO(adoption);
    }

    public AdoptionDTO approve(Long id) {
        Adoption adoption = adoptionRepository.findById(id).orElseThrow();
        adoption.setIsApproved(true);
        return AdoptionMapper.toDTO(adoptionRepository.save(adoption));
    }

    public AdoptionDTO reject(Long id) {
        Adoption adoption = adoptionRepository.findById(id).orElseThrow();
        adoption.setIsApproved(false);
        return AdoptionMapper.toDTO(adoptionRepository.save(adoption));
    }

    public void delete(Long id) {
        Adoption adoption = adoptionRepository.findById(id).orElseThrow();
        adoptionRepository.delete(adoption);
    }

    public List<AdoptionDTO> getByAdopter(Long adopterId) {
        PetOwner adopter = petOwnerRepository.findById(adopterId).orElseThrow();
        return adoptionRepository.findAll().stream()
                .filter(a -> a.getAdopter().getPetOwnerId().equals(adopter.getPetOwnerId()))
                .map(AdoptionMapper::toDTO)
                .toList();
    }
}
