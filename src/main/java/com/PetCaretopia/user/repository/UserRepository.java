package com.PetCaretopia.user.repository;


import com.PetCaretopia.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmail(String email);
    Optional<User> findByUserPhoneNumber(String phoneNumber);
    Optional<User> findByName(String userName);
    Optional<User> findByUserEmailAndUserPhoneNumber(String email, String phone);

}
