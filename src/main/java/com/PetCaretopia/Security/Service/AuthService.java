package com.PetCaretopia.Security.Service;



import com.PetCaretopia.Security.Dto.AuthRequest;
import com.PetCaretopia.Security.Dto.AuthResponse;
import com.PetCaretopia.Security.Dto.RegisterRequest;
import com.PetCaretopia.Security.Dto.ResetPasswordRequest;
import com.PetCaretopia.Security.Util.GetAgeUtil;
import com.PetCaretopia.Security.Util.JwtUtil;
import com.PetCaretopia.user.entity.ServiceProvider;
import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.user.repository.ServiceProviderRepository;
import com.PetCaretopia.user.repository.UserRepository;
import com.PetCaretopia.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final ServiceProviderRepository serviceProviderRepository;

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final GetAgeUtil getAgeUtil;


    public AuthResponse register(RegisterRequest request){

            User newUser = User.builder()
                    .name(request.getName())
                    .userEmail(request.getEmail())
                    .userPhoneNumber(request.getPhoneNumber())
                    .birthDate(request.getBirthDate())
                    .userPassword(passwordEncoder.encode(request.getPassword()))
                    .userRole(request.getRole())
                    .userGender(request.getGender())
                    .userAddress(request.getAddress())
                    .userLastLoginDate(request.getLastLoginDate())
                    .userStatus(User.Status.ACTIVE)
                    .userCreationalDate(LocalDateTime.now())
                    .build();
            User savedUser = userService.saveUser(newUser);
            if(newUser.getUserRole() == User.Role.SERVICE_PROVIDER){
                ServiceProvider serviceProvider = ServiceProvider.builder()
                        .user(newUser)
                        .serviceProviderType(ServiceProvider.ServiceProviderType.OTHER)
                        .serviceProviderSalary(BigDecimal.valueOf(0))
                        .serviceProviderExperience(0)
                        .creationalDate(LocalDateTime.now())
                        .build();
                serviceProviderRepository.save(serviceProvider);
            }
            return new AuthResponse("Registered Successfully !");


    }

    public AuthResponse login(AuthRequest request){


            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())

            );

            User user = userRepository.findByUserEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
            String token = jwtUtil.generateToken(user.getUserID(), user.getName(),getAgeUtil.getAge(user.getBirthDate()) ,user.getUserEmail(),user.getUserRole().name(),user.getUserPhoneNumber());
            user.setUserLastLoginDate(LocalDateTime.now());
            userRepository.save(user);
           return new AuthResponse(token);


    }

    public AuthResponse resetPassword(ResetPasswordRequest request) {

            var optionalUser = userRepository.findByUserEmailAndUserPhoneNumber(request.getEmail(), request.getPhoneNumber());
            if(optionalUser.isEmpty()) throw new RuntimeException("User not found!");
            if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
                throw new RuntimeException("Passwords do not match!");
            }
            var user = optionalUser.get();
            user.setUserPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            return new AuthResponse("Password Changed Successfully !");

    }
    public boolean tokenIsValid(String token){
            if(token.startsWith("Bearer ")){
                token = token.substring(7);
                return jwtUtil.isTokenValid(token);
            }
            return false;

    }
//
//    }

}
