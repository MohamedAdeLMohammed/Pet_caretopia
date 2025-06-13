package com.PetCaretopia.pet.service;

import com.PetCaretopia.pet.DTO.BreedingRequestDTO;
import com.PetCaretopia.pet.Mapper.BreedingRequestMapper;
import com.PetCaretopia.pet.entity.*;
import com.PetCaretopia.pet.repository.BreedingRequestRepository;
import com.PetCaretopia.pet.repository.PetRepository;
import com.PetCaretopia.user.entity.PetOwner;
import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.user.repository.PetOwnerRepository;
import com.PetCaretopia.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BreedingRequestService {

    @Autowired
    private BreedingRequestRepository repository;

    @Autowired
    private PetRepository petRepo;

    @Autowired
    private PetOwnerRepository ownerRepo;

    @Autowired
    private BreedingRequestMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetOwnerRepository petOwnerRepository;

    @Autowired
    private UserRepository userRepo;


    public BreedingRequestDTO createRequest(Long malePetId, Long femalePetId, String emailFromToken) {
        // 1. جلب الحيوانات
        Pet male = petRepo.findById(malePetId)
                .orElseThrow(() -> new IllegalArgumentException("Male pet not found"));

        Pet female = petRepo.findById(femalePetId)
                .orElseThrow(() -> new IllegalArgumentException("Female pet not found"));

        // 2. جلب المستخدم الحالي
        User user = userRepo.findByUserEmail(emailFromToken)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PetOwner requester = ownerRepo.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Requester not found"));

        // 3. تحديد أصحاب الحيوانات
        PetOwner maleOwner = male.getOwner();
        PetOwner femaleOwner = female.getOwner();

        // 4. منع التزاوج بين حيوانات نفس المالك
        if (maleOwner.getPetOwnerId().equals(femaleOwner.getPetOwnerId())) {
            throw new IllegalArgumentException("You cannot breed your own pets.");
        }

        // 5. التأكد من النوع
        if (male.getGender() != User.Gender.MALE || female.getGender() != User.Gender.FEMALE) {
            throw new IllegalArgumentException("Breeding allowed only between male and female pets.");
        }

        // 6. التأكد من الحالة
        if (!male.isAvailableForBreeding() || !female.isAvailableForBreeding()) {
            throw new IllegalArgumentException("One or both pets are not available for breeding.");
        }

        // 7. منع تكرار الريكوست لنفس الذكر والأنثى في نفس اليوم
        boolean exists = repository.existsByMalePetAndFemalePetAndRequestDate(male, female, LocalDate.now());
        if (exists) {
            throw new IllegalArgumentException("Breeding request already exists for today.");
        }

        // 8. تحديد الطرف المستقبل للطلب
        PetOwner receiver = requester.getPetOwnerId().equals(maleOwner.getPetOwnerId())
                ? femaleOwner
                : maleOwner;

        // 9. إنشاء الريكوست
        BreedingRequest request = new BreedingRequest();
        request.setMalePet(male);
        request.setFemalePet(female);
        request.setRequester(requester);
        request.setReceiver(receiver);
        request.setRequestDate(LocalDate.now());
        request.setStatus(BreedingStatus.PENDING);

        return mapper.toDTO(repository.save(request));
    }



    public void acceptRequest(Long requestId) {
        BreedingRequest request = repository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        Pet male = request.getMalePet();
        Pet female = request.getFemalePet();

        if (!male.isAvailableForBreeding() || !female.isAvailableForBreeding()) {
            throw new IllegalStateException("One or both pets are not available for breeding.");
        }

        request.setStatus(BreedingStatus.ACCEPTED);
        repository.save(request);

        male.setAvailableForBreeding(false);
        female.setAvailableForBreeding(false);

        petRepo.save(male);
        petRepo.save(female);
    }


    public void rejectRequest(Long requestId) {
        BreedingRequest request = repository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        request.setStatus(BreedingStatus.REJECTED);
        repository.save(request);
    }

    public List<BreedingRequestDTO> getRequestsForUser(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PetOwner owner = petOwnerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Pet owner not found"));

        return repository.findByReceiver_PetOwnerId(owner.getPetOwnerId())
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<BreedingRequestDTO> getSentRequestsForUser(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PetOwner owner = petOwnerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Pet owner not found"));

        return repository.findByRequester_PetOwnerId(owner.getPetOwnerId())
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
    public List<Pet> getAllAvailablePetsForBreeding() {
        return petRepo.findByIsAvailableForBreedingTrue();
    }


    public List<BreedingRequestDTO> getAllRequestsForAdmin(String status) {
        List<BreedingRequest> requests;

        if (status == null || status.isBlank()) {
            requests = repository.findAll();
        } else {
            BreedingStatus parsedStatus;
            try {
                parsedStatus = BreedingStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status value. Must be: PENDING, ACCEPTED, or REJECTED.");
            }
            requests = repository.findByStatus(parsedStatus);
        }

        return requests.stream()
                .map(mapper::toDTO)
                .toList();
    }



}
