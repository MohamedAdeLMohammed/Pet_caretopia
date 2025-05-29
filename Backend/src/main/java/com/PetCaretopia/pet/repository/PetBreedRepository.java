package com.PetCaretopia.pet.repository;

import com.PetCaretopia.pet.entity.PetBreed;
import com.PetCaretopia.pet.entity.PetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetBreedRepository extends JpaRepository<PetBreed, Long> {
    Optional<PetBreed> findByBreedName(String breedName);
    Optional<PetBreed> findByBreedNameAndPetType(String breedName, PetType type);
    List<PetBreed> findByPetType(PetType petType);

}
