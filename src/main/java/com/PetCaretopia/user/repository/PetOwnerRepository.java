package com.PetCaretopia.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.PetCaretopia.user.entity.PetOwner;

import java.util.Optional;

public interface PetOwnerRepository extends JpaRepository<PetOwner, Long> {
    Optional<PetOwner> findByUser_UserID(Long userID);
    boolean existsByUser_UserID(Long userId);

}
