package com.PetCaretopia.health.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.PetCaretopia.health.entity.PetVaccine;
import com.PetCaretopia.pet.entity.Pet;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetVaccineRepository extends JpaRepository<PetVaccine, Long> {
    List<PetVaccine> findByPet(Pet pet);
    List<PetVaccine> findByPet_PetID(Long petId);
    List<PetVaccine> findByVaccine_Name(String vaccineName);
    List<PetVaccine> findByVaccine_Id(Long VaccineId);
    List<PetVaccine> findByVaccine_Type_Name(String vaccineTypeName);
    List<PetVaccine> findByVaccine_Type_Id(Long vaccineTypeId);
    Optional<PetVaccine> findByPet_PetIDAndVaccine_Id(Long petId, Long vaccineId);


}
