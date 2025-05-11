package com.PetCaretopia.Security.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> testUser(){
        return ResponseEntity.ok("Hello User From Spring Security!");
    }
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> testAdmin(){
        return ResponseEntity.ok("Hello Admin From Spring Security!");
    }
    @GetMapping("/pet-owner")
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<String> testPetOwner(){
        return ResponseEntity.ok("Hello Pet Owner From Spring Security!");
    }
    @GetMapping("/service-provider")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<String> testServiceProvider(){
        return ResponseEntity.ok("Hello Service Provider From Spring Security!");
    }
}
