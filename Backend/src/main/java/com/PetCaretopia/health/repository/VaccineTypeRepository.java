package com.PetCaretopia.health.repository;

import com.PetCaretopia.health.entity.VaccineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VaccineTypeRepository extends JpaRepository<VaccineType, Long> {
    Optional<VaccineType> findByName(String name);
}
