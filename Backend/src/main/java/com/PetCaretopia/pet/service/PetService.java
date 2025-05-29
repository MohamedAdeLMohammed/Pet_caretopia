package com.PetCaretopia.pet.service;

import com.PetCaretopia.Security.Service.CustomUserDetails;
import com.PetCaretopia.pet.DTO.PetDTO;
import com.PetCaretopia.pet.Mapper.PetMapper;
import com.PetCaretopia.pet.entity.*;
import com.PetCaretopia.pet.repository.*;
import com.PetCaretopia.shared.SharedImageUploadService;
import com.PetCaretopia.user.entity.PetOwner;
import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.user.repository.PetOwnerRepository;
import com.PetCaretopia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;

import java.nio.file.AccessDeniedException;
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
    private final UserRepository userRepository;
    private final PetMapper petMapper;
    public PetDTO createPet(PetDTO dto, MultipartFile imageFile) {
        PetType type = petTypeRepository.findByTypeName(dto.getPetTypeName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid pet type"));

        PetBreed breed = petBreedRepository.findByBreedNameAndPetType(dto.getPetBreedName(), type)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pet breed"));

        PetOwner owner = null;
        Shelter shelter = null;

        if (dto.getOwnerId() != null) {
            owner = petOwnerRepository.findById(dto.getOwnerId()).orElse(null);
            if (owner == null) {
                User user = userRepository.findById(dto.getOwnerId())
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));
                if (user.getUserRole() != User.Role.PET_OWNER) {
                    user.setUserRole(User.Role.PET_OWNER);
                }
                owner = new PetOwner();
                owner.setUser(user);
                user.setPetOwner(owner);
                userRepository.save(user);
            }
        }

        if (dto.getShelterId() != null) {
            shelter = shelterRepository.findById(dto.getShelterId())
                    .orElseThrow(() -> new IllegalArgumentException("Shelter not found"));
        }

        // 🛑 تأكد إن مش الاتنين متحددين
        if (owner != null && shelter != null) {
            throw new IllegalArgumentException("Pet cannot have both owner and shelter.");
        }

        Pet pet = PetMapper.toEntity(dto, type, breed, owner, shelter);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = imageUploadService.uploadMultipartFile(imageFile);
            pet.setImageUrl(imageUrl);
        }

        return PetMapper.toDTO(petRepository.save(pet));
    }

    public List<PetDTO> getAllPets() {
        return petRepository.findAll().stream()
                .map(PetMapper::toDTO)
                .toList();
    }

    public PetDTO updatePet(Long id, PetDTO dto, MultipartFile imageFile, CustomUserDetails principal) throws AccessDeniedException {
        Pet existingPet = petRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found"));

        boolean isAdmin = principal.getRole().equals("ADMIN");

        if (!isAdmin) {
            if (existingPet.getOwner() == null || !existingPet.getOwner().getPetOwnerId().equals(dto.getOwnerId())) {
                throw new AccessDeniedException("You are not allowed to edit this pet.");
            }
        }
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

    public void deletePet(Long id, CustomUserDetails principal) throws AccessDeniedException {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found"));

        boolean isAdmin = "ADMIN".equals(principal.getRole());

        if (!isAdmin) {
            PetOwner owner = petOwnerRepository.findByUser_UserID(principal.getUserId())
                    .orElseThrow(() -> new AccessDeniedException("You are not a PetOwner"));

            if (pet.getOwner() == null || !pet.getOwner().getPetOwnerId().equals(owner.getPetOwnerId())) {
                throw new AccessDeniedException("You are not allowed to delete this pet.");
            }
        }

        petRepository.delete(pet);
    }
    public List<PetDTO> getMyPets(Long userId) {
        return petRepository.findByOwner_User_UserID(userId).stream()
                .map(p -> petMapper.toDTO(p))
                .toList();
    }


    public List<PetDTO> getPetsByType(String typeName) {
        PetType type = petTypeRepository.findByTypeName(typeName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid type"));
        return petRepository.findByPetType(type).stream().map(PetMapper::toDTO).toList();
    }

    public void offerForAdoption(Long petId, CustomUserDetails principal) throws AccessDeniedException {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found"));

        // تحقق إن المستخدم يملك الحيوان ده
        boolean isOwner = pet.getOwner() != null &&
                pet.getOwner().getUser().getUserID().equals(principal.getUserId());

        boolean isShelter = pet.getShelter() != null &&
                pet.getShelter().getCreatedBy().equals(principal.getUserId());

        if (!isOwner && !isShelter && !principal.getRole().equals("ADMIN")) {
            throw new AccessDeniedException("You are not allowed to offer this pet for adoption");
        }

        pet.setAvailableForAdoption(true);
        petRepository.save(pet);
    }
    public List<PetDTO> getAvailableForAdoption() {
        return petRepository.findByIsAvailableForAdoptionTrue().stream()
                .map(PetMapper::toDTO)
                .toList();
    }


}
