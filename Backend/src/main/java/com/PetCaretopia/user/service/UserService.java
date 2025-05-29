package com.PetCaretopia.user.service;



import com.PetCaretopia.Chat.Model.Account;
import com.PetCaretopia.Chat.Repository.AccountRepository;

import com.PetCaretopia.Security.Util.GetAgeUtil;
import com.PetCaretopia.order.entity.ProductImage;
import com.PetCaretopia.shared.SharedImageUploadService;
import com.PetCaretopia.user.DTO.UserDTO;
import com.PetCaretopia.user.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.user.repository.UserRepository;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final UserMapper userMapper;
    private final SharedImageUploadService imageUploadService;
    public UserService(UserRepository userRepository, AccountRepository accountRepository, UserMapper userMapper, SharedImageUploadService imageUploadService) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.userMapper = userMapper;
        this.imageUploadService = imageUploadService;
    }

    public User saveUser(User user) {
        if (user.getUserStatus() == null) {
            user.setUserStatus(User.Status.ACTIVE);
        }
        if (user.getUserCreationalDate() == null) {
            user.setUserCreationalDate(LocalDateTime.now());
        }

        // Prevent duplicate email
        // Check for duplicate email only if it's used by another user
        userRepository.findByUserEmail(user.getUserEmail())
                .filter(existing -> !existing.getUserID().equals(user.getUserID()))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Email is already in use");
                });

        // Check for duplicate phone number only if it's used by another user
        userRepository.findByUserPhoneNumber(user.getUserPhoneNumber())
                .filter(existing -> !existing.getUserID().equals(user.getUserID()))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Phone number is already in use");
                });


        return userRepository.save(user);
    }
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByUserEmail(email);
    }
    public UserDTO getUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("User Not found!"));
        return userMapper.toUserDTO(user);

    }
    public Optional<User> getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByUserPhoneNumber(phoneNumber);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByName(username);
    }
    public UserDTO updateUser(Long id, UserDTO user, MultipartFile image){
        User existUser = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("User Not found!"));
        if(user != null){
            if(user.getBirthDate() != null){
                existUser.setBirthDate(user.getBirthDate());
            }
            if(user.getName() != null){
                existUser.setName(user.getName());
            }
            if(user.getUserGender() != null){
                existUser.setUserGender(user.getUserGender());
            }
            if(user.getUserAddress() != null){
                existUser.setUserAddress(user.getUserAddress());
            }
            if(user.getUserDetails() != null){
                existUser.setUserDetails(user.getUserDetails());
            }
        }
        if(image != null && !image.isEmpty()){
            String imageUrl = imageUploadService.uploadMultipartFile(image);
            existUser.setUserProfileImage(imageUrl);
        }
        else {
            existUser.setUserProfileImage(existUser.getUserProfileImage());
        }
        saveUser(existUser);
        return userMapper.toUserDTO(existUser);
    }
    public String deleteUser(Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("User Not found!"));
        Account userAccount = accountRepository.findByUsername(user.getUserEmail()).orElseThrow(()->new IllegalArgumentException("Account Not found!"));
        accountRepository.delete(userAccount);
        userRepository.delete(user);
        return "User Deleted!";
    }
    public String changeUserStatus(Long id,UserDTO status){
        User user = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("User Not Found!"));
        user.setUserStatus(status.getUserStatus());
        userRepository.save(user);
        return "User Status is : "+status.getUserStatus().name();
    }
    public String upgradeToAdmin(Long userId){
        var user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("User Not Found !"));
        if(user.getUserRole().equals(User.Role.ADMIN)){
            throw new IllegalArgumentException("This user is already Admin !");
        }
        if(!user.getUserRole().equals(User.Role.USER)){
            throw new IllegalArgumentException("Only User can became Admin !");
        }
        user.setUserRole(User.Role.ADMIN);
        userRepository.save(user);
        return "This user became new Admin !";
    }

}
