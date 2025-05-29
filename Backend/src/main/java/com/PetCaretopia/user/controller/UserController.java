package  com.PetCaretopia.user.controller;



import com.PetCaretopia.Security.Service.CustomUserDetails;
import com.PetCaretopia.user.DTO.UserDTO;
import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/email/{email}")
    public Optional<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }
    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id , @RequestPart(value = "user",required = false) UserDTO user, @RequestPart(value = "image", required = false) MultipartFile image,@AuthenticationPrincipal CustomUserDetails userDetails){
        Long authId = userDetails.getUserId();
        if(!authId.equals(id)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied !");
        }
        return ResponseEntity.ok(userService.updateUser(id, user,image));

    }
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id,@AuthenticationPrincipal CustomUserDetails userDetails){
        Long authId = userDetails.getUserId();
        if(!authId.equals(id)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied !");
        }
        return ResponseEntity.ok(userService.deleteUser(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/user/{id}")
    public ResponseEntity<String> changeUserStatus(@PathVariable Long id , @RequestBody UserDTO status){
        return ResponseEntity.ok(userService.changeUserStatus(id,status));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/toAdmin/user/{id}")
    public ResponseEntity<String> upgradeUserToAdmin(@PathVariable Long id,@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(userService.upgradeToAdmin(id));
    }
}

