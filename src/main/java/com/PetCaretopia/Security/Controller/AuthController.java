package com.PetCaretopia.Security.Controller;



import com.PetCaretopia.Chat.Service.AccountService;
import com.PetCaretopia.Security.Dto.AuthRequest;
import com.PetCaretopia.Security.Dto.AuthResponse;
import com.PetCaretopia.Security.Dto.RegisterRequest;
import com.PetCaretopia.Security.Dto.ResetPasswordRequest;
import com.PetCaretopia.Security.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request){
        return ResponseEntity.ok(authService.login(request));
    }
    @PostMapping("/reset-password")
    public ResponseEntity<AuthResponse> resetPassword(@RequestBody ResetPasswordRequest request){
      return ResponseEntity.ok(authService.resetPassword(request));
    }
    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token){
        boolean isValid = authService.tokenIsValid(token);
        return ResponseEntity.ok().body(Map.of("valid",isValid));
    }
    @GetMapping("/welcome")
    public ResponseEntity<String> welcome(){
        return ResponseEntity.ok("Welcome to the Spring Security Application!");
    }


}
