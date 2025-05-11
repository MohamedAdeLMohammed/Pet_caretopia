package com.PetCaretopia.user.service;



import org.springframework.stereotype.Service;

import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        if (user.getUserStatus() == null) {
            user.setUserStatus(User.Status.ACTIVE);
        }
        if (user.getUserCreationalDate() == null) {
            user.setUserCreationalDate(LocalDate.now());
        }

        // Prevent duplicate email
        if (userRepository.findByUserEmail(user.getUserEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // Prevent duplicate phone number
        if (userRepository.findByUserPhoneNumber(user.getUserPhoneNumber()).isPresent()) {
            throw new IllegalArgumentException("Phone number is already in use");
        }

        return userRepository.save(user);
    }
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByUserEmail(email);
    }

    public Optional<User> getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByUserPhoneNumber(phoneNumber);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByName(username);
    }
}
