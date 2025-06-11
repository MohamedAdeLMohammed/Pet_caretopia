package com.PetCaretopia.pet.repository;

import com.PetCaretopia.pet.entity.BreedingRequest;
import com.PetCaretopia.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BreedingRequestRepository extends JpaRepository<BreedingRequest, Long> {
    List<BreedingRequest> findByReceiver_PetOwnerId(Long receiverId);
    List<BreedingRequest> findByRequester_PetOwnerId(Long requesterId);

    // ✅ جديد: التحقق من تكرار الطلب
    boolean existsByMalePetAndFemalePetAndRequestDate(Pet male, Pet female, LocalDate date);
}

