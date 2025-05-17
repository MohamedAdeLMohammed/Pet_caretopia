package com.PetCaretopia.pet.repository;

import com.PetCaretopia.pet.entity.Pet;
import com.PetCaretopia.pet.entity.PetBreed;
import com.PetCaretopia.pet.entity.PetType;
import com.PetCaretopia.pet.entity.Shelter;
import com.PetCaretopia.user.entity.PetOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByPetType(PetType petType);
    List<Pet> findByPetBreed(PetBreed petBreed);
    List<Pet> findByShelter(Shelter shelter);
    List<Pet> findByOwner(PetOwner owner);
    List<Pet> findByOwner_User_UserID(Long userId);

    List<Pet> findByIsAvailableForAdoptionTrue();
}
