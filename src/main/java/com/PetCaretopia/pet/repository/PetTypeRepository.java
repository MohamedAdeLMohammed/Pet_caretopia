package com.PetCaretopia.pet.repository;

import com.PetCaretopia.pet.entity.PetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetTypeRepository extends JpaRepository<PetType, Long> {
    Optional<PetType> findByTypeName(String typeName);
    boolean existsByTypeName(String typeName);
}
