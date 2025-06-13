package com.PetCaretopia.pet.service;

import com.PetCaretopia.Security.Service.CustomUserDetails;
import com.PetCaretopia.pet.DTO.AdoptionDTO;
import com.PetCaretopia.pet.DTO.AdoptionOfferDTO;
import com.PetCaretopia.pet.Mapper.AdoptionMapper;
import com.PetCaretopia.pet.entity.Adoption;
import com.PetCaretopia.pet.entity.AdoptionStatus;
import com.PetCaretopia.pet.entity.Pet;
import com.PetCaretopia.pet.entity.Shelter;

import com.PetCaretopia.pet.repository.AdoptionRepository;
import com.PetCaretopia.pet.repository.PetRepository;
import com.PetCaretopia.pet.repository.ShelterRepository;
import com.PetCaretopia.user.entity.PetOwner;
import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.user.repository.PetOwnerRepository;
import com.PetCaretopia.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.PetCaretopia.user.entity.User.Role.PET_OWNER;
import static com.PetCaretopia.user.entity.User.Role.USER;

@Service
@RequiredArgsConstructor
public class AdoptionService {

    private final UserRepository userRepository;
    private final AdoptionRepository adoptionRepository;
    private final PetRepository petRepository;
    private final PetOwnerRepository petOwnerRepository;
    private final ShelterRepository shelterRepository;

    @Transactional
    public AdoptionDTO submitRequest(AdoptionDTO dto, CustomUserDetails principal) {
        Pet pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with ID: " + dto.getPetId()));

        if (pet.getOwner() != null && pet.getOwner().getUser().getUserID().equals(principal.getUserId())) {
            throw new IllegalArgumentException("You cannot adopt your own pet.");
        }

        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        dto.setRequesterUserId(principal.getUserId());

        Adoption adoption = AdoptionMapper.toEntity(dto, pet, null, null, pet.getShelter(), user);

        adoption.setStatus(AdoptionStatus.PENDING);
        adoption.setAdoptionDate(dto.getAdoptionDate() != null ? dto.getAdoptionDate() : LocalDate.now());
        adoption.setCreatedAt(LocalDateTime.now());

        return AdoptionMapper.toDTO(adoptionRepository.save(adoption));
    }

    public List<AdoptionDTO> getByPetId(Long petId) {
        return adoptionRepository.findByPet_PetID(petId).stream()
                .map(AdoptionMapper::toDTO)
                .toList();
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

    @Transactional
    public AdoptionDTO approve(Long id, CustomUserDetails principal) {
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adoption not found"));

        if (adoption.getStatus() == AdoptionStatus.APPROVED || adoption.getStatus() == AdoptionStatus.REJECTED) {
            throw new IllegalStateException("This adoption request has already been processed.");
        }

        Pet pet = adoption.getPet();

        boolean isAdmin = principal.getRole().equals("ADMIN");

        if (!isAdmin) {
            if (pet.getOwner() == null || !pet.getOwner().getUser().getUserID().equals(principal.getUserId())) {
                throw new AccessDeniedException("You can only approve adoptions for your own pets.");
            }
        }

        User user = userRepository.findById(adoption.getCreatedBy())
                .orElseThrow(() -> new EntityNotFoundException("User who submitted the adoption request not found"));

        // Get or create PetOwner
        PetOwner adopter = petOwnerRepository.findByUser_UserID(user.getUserID())
                .orElseGet(() -> {
                    PetOwner newOwner = new PetOwner();
                    newOwner.setUser(user);
                    PetOwner saved = petOwnerRepository.save(newOwner);

                    user.setUserRole(PET_OWNER);
                    userRepository.save(user);
                    return saved;
                });

        PetOwner previousOwner = pet.getOwner();

        // Adopt logic
        pet.adopt(adopter); // ← this sets new owner
        pet.setAvailableForAdoption(false);

        // Set adoption info
        adoption.setAdopter(adopter);
        adoption.setStatus(AdoptionStatus.APPROVED);
        adoption.setPreviousOwner(previousOwner);

        List<Adoption> otherRequests = adoptionRepository.findByPet(pet).stream()
                .filter(other -> !other.getId().equals(id) && other.getStatus() == AdoptionStatus.PENDING)
                .peek(other -> other.setStatus(AdoptionStatus.REJECTED))
                .toList();

        // Save everything
        adoptionRepository.saveAll(otherRequests);
        petRepository.save(pet);
        Adoption saved = adoptionRepository.save(adoption);

        return AdoptionMapper.toDTO(saved);
    }
    @Transactional
    public AdoptionDTO reject(Long id, CustomUserDetails principal) {
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adoption not found"));

        Pet pet = adoption.getPet();

        if (adoption.getStatus() == AdoptionStatus.APPROVED || adoption.getStatus() == AdoptionStatus.REJECTED) {
            throw new IllegalStateException("This adoption request has already been processed.");
        }

        boolean isAdmin = principal.getRole().equals("ADMIN");

        if (!isAdmin) {
            if (pet.getOwner() == null || !pet.getOwner().getUser().getUserID().equals(principal.getUserId())) {
                throw new AccessDeniedException("You can only reject adoptions for your own pets.");
            }
        }

        adoption.setStatus(AdoptionStatus.REJECTED);
        Adoption savedAdoption = adoptionRepository.save(adoption);

        Long requesterUserId = adoption.getCreatedBy();
        if (requesterUserId != null) {
            User user = userRepository.findById(requesterUserId).orElse(null);

            if (user != null && petOwnerRepository.existsByUser_UserID(user.getUserID())) {
                PetOwner adopter = petOwnerRepository.findByUser_UserID(user.getUserID()).orElse(null);

                if (adopter != null) {
                    List<Adoption> otherAdoptions = adoptionRepository.findByAdopter_PetOwnerId(adopter.getPetOwnerId()).stream()
                            .filter(a -> !Objects.equals(a.getId(), adoption.getId()))
                            .filter(a -> a.getStatus() != AdoptionStatus.REJECTED)
                            .toList();

                    if (otherAdoptions.isEmpty()) {
                        petOwnerRepository.delete(adopter);

                        // ✅ نرجع الـ Role إلى USER
                        user.setUserRole(USER);
                        userRepository.save(user);
                    }
                }
            }
        }

        return AdoptionMapper.toDTO(savedAdoption);
    }

    @Transactional
    public void delete(Long id) {
        Adoption adoption = adoptionRepository.findById(id).orElseThrow();
        adoptionRepository.delete(adoption);
    }

    public List<AdoptionDTO> getByAdopter(Long adopterId) {
        return adoptionRepository.findByAdopter_PetOwnerId(adopterId).stream()
                .map(AdoptionMapper::toDTO)
                .toList();
    }

    public List<AdoptionDTO> getByOwnerPets(Long userId) {
        PetOwner owner = petOwnerRepository.findByUser_UserID(userId)
                .orElseThrow(() -> new EntityNotFoundException("PetOwner not found"));

        return adoptionRepository.findAll().stream()
                .filter(adoption -> {
                    Pet pet = adoption.getPet();
                    PetOwner currentOwner = pet.getOwner();
                    PetOwner previousOwner = adoption.getPreviousOwner();
                    Long currentOwnerId = (currentOwner != null) ? currentOwner.getPetOwnerId() : null;
                    Long previousOwnerId = (previousOwner != null) ? previousOwner.getPetOwnerId() : null;

                    boolean isOwner = (currentOwnerId != null && currentOwnerId.equals(owner.getPetOwnerId()))
                            || (previousOwnerId != null && previousOwnerId.equals(owner.getPetOwnerId()));

                    boolean isRequester = adoption.getCreatedBy() != null
                            && adoption.getCreatedBy().equals(userId);

                    return isOwner || isRequester;
                })
                .map(AdoptionMapper::toDTO)
                .toList();
    }
    public List<AdoptionDTO> getMyRequests(Long userId, AdoptionStatus status) {
        List<Adoption> adoptions;

        if (status != null) {
            adoptions = adoptionRepository.findByRequesterUserIdAndStatus(userId, status);
        } else {
            adoptions = adoptionRepository.findByRequesterUserId(userId);
        }

        return adoptions.stream()
                .map(AdoptionMapper::toDTO)
                .toList();
    }

    public AdoptionDTO offerAdoption(AdoptionOfferDTO dto, CustomUserDetails principal) {
        Pet pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new EntityNotFoundException("Pet not found"));

        if (pet.getOwner() == null || !pet.getOwner().getUser().getUserID().equals(principal.getUserId())) {
            throw new AccessDeniedException("You can only offer adoption for your own pets.");
        }

        User targetUser = userRepository.findById(dto.getTargetUserId())
                .orElseThrow(() -> new EntityNotFoundException("Target user not found"));

        Adoption adoption = new Adoption();
        adoption.setPet(pet);
        adoption.setRequesterUserId(principal.getUserId());
        adoption.setCreatedBy(principal.getUserId());
        adoption.setMessage(dto.getMessage());
        adoption.setAdoptionDate(LocalDate.now());
        adoption.setCreatedAt(LocalDateTime.now());
        adoption.setStatus(AdoptionStatus.PENDING);

        return AdoptionMapper.toDTO(adoptionRepository.save(adoption));
    }

}
