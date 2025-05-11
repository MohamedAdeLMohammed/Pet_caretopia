package com.PetCaretopia.health.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PetCaretopia.health.entity.Breeding;
import com.PetCaretopia.pet.entity.Pet;

import java.util.List;

@Repository
public interface BreedingRepository extends JpaRepository<Breeding, Long> {
    List<Breeding> findByMalePetOrFemalePet(Pet malePet, Pet femalePet);
}
