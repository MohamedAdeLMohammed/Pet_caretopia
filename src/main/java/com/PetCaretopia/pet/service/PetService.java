package com.PetCaretopia.pet.service;

import com.PetCaretopia.pet.DTO.PetDTO;
import com.PetCaretopia.pet.Mapper.PetMapper;
import com.PetCaretopia.pet.entity.*;
import com.PetCaretopia.pet.repository.*;
import com.PetCaretopia.shared.SharedImageUploadService;
import com.PetCaretopia.user.entity.PetOwner;
import com.PetCaretopia.user.repository.PetOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final PetTypeRepository petTypeRepository;
    private final PetBreedRepository petBreedRepository;
    private final PetOwnerRepository petOwnerRepository;
    private final ShelterRepository shelterRepository;
    private final SharedImageUploadService imageUploadService;

    public PetDTO createPet(PetDTO dto, MultipartFile imageFile) {

        // ✅ التحقق من نوع الحيوان
        PetType type = petTypeRepository.findByTypeName(dto.getPetTypeName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid pet type"));

        // ✅ التحقق من السلالة
        PetBreed breed = petBreedRepository.findByBreedNameAndPetType(dto.getPetBreedName(), type)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pet breed"));

        // ✅ جلب المالك إن وُجد
        PetOwner owner = null;
        if (dto.getOwnerId() != null) {
            owner = petOwnerRepository.findById(dto.getOwnerId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid owner ID"));
        }

        // ✅ جلب الملجأ إن وُجد
        Shelter shelter = null;
        if (dto.getShelterId() != null) {
            shelter = shelterRepository.findById(dto.getShelterId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid shelter ID"));
        }

        // ✅ إنشاء الكيان
        Pet pet = PetMapper.toEntity(dto, type, breed, owner, shelter);

        // ✅ رفع الصورة إن وُجدت
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = imageUploadService.uploadMultipartFile(imageFile);
            pet.setImageUrl(imageUrl);
        }

        // ✅ حفظ وإرجاع الـ DTO
        return PetMapper.toDTO(petRepository.save(pet));
    }

    public List<PetDTO> getAllPets() {
        return petRepository.findAll().stream()
                .map(PetMapper::toDTO)
                .toList();
    }

    public PetDTO updatePet(Long id, PetDTO dto, MultipartFile imageFile) {
        Pet existingPet = petRepository.findById(id).orElseThrow();

        PetType type = petTypeRepository.findByTypeName(dto.getPetTypeName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid type"));

        PetBreed breed = petBreedRepository.findByBreedNameAndPetType(dto.getPetBreedName(), type)
                .orElseThrow(() -> new IllegalArgumentException("Invalid breed"));

        PetOwner owner = dto.getOwnerId() != null ? petOwnerRepository.findById(dto.getOwnerId()).orElse(null) : null;
        Shelter shelter = dto.getShelterId() != null ? shelterRepository.findById(dto.getShelterId()).orElse(null) : null;

        existingPet.setPetName(dto.getPetName());
        existingPet.setPetType(type);
        existingPet.setPetBreed(breed);
        existingPet.setOwner(owner);
        existingPet.setShelter(shelter);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = imageUploadService.uploadMultipartFile(imageFile);
            existingPet.setImageUrl(imageUrl);
        }

        return PetMapper.toDTO(petRepository.save(existingPet));
    }

    public PetDTO getPetById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found"));
        return PetMapper.toDTO(pet);
    }
    public void deletePet(Long id) {
        Pet pet = petRepository.findById(id).orElseThrow();
        petRepository.delete(pet);
    }
    public List<PetDTO> getPetsByType(String typeName) {
        PetType type = petTypeRepository.findByTypeName(typeName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid type"));
        return petRepository.findByPetType(type).stream().map(PetMapper::toDTO).toList();
    }

}
