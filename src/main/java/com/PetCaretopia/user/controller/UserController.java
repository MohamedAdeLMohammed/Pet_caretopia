package  com.PetCaretopia.user.controller;



import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.user.service.UserService;
import org.springframework.web.bind.annotation.*;

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


    }

