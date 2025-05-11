package com.PetCaretopia.health.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PetCaretopia.health.entity.PetVaccine;
import com.PetCaretopia.pet.entity.Pet;

import java.util.List;

@Repository
public interface PetVaccineRepository extends JpaRepository<PetVaccine, Long> {
    List<PetVaccine> findByPet(Pet pet);
}
