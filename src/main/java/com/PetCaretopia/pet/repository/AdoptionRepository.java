package com.PetCaretopia.pet.repository;

import com.PetCaretopia.pet.entity.Adoption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long> {
    List<Adoption> findByShelterId(Long shelterId);
    List<Adoption> findByAdopter_PetOwnerId(Long petOwnerId);
    List<Adoption> findByIsApproved(Boolean isApproved);
}
