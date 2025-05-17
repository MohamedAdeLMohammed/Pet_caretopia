package com.PetCaretopia;

import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static com.PetCaretopia.user.entity.User.Gender.MALE;

@Configuration
@RequiredArgsConstructor
public class AdminBootstrap {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Bean
    public CommandLineRunner createAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@petcaretopia.com";

            if (userRepository.findByUserEmail(adminEmail).isEmpty()) {
                User admin = User.builder()
                        .name("Super Admin")
                        .userEmail(adminEmail)
                        .userPhoneNumber("01000000000")
                        .birthDate(LocalDate.of(1990, 1, 1))
                        .userPassword(passwordEncoder.encode("admin123")) // 👈 choose a secure password
                        .userRole(User.Role.ADMIN)
                        .userGender(MALE)
                        .userAddress("HQ")
                        .userLastLoginDate(LocalDate.now().atStartOfDay())
                        .build();

                userRepository.save(admin);
                System.out.println("✅ Admin user created: " + adminEmail);
            } else {
                System.out.println("ℹ️ Admin already exists: " + adminEmail);
            }
        };
    }
}
