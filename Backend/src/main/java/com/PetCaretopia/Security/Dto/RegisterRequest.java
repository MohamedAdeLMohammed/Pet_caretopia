package com.PetCaretopia.Security.Dto;

import com.PetCaretopia.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private LocalDate birthDate;
    private User.Role role;
    private User.Gender gender;
    private String address;
    private LocalDateTime lastLoginDate = LocalDateTime.now();

}
