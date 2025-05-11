package com.PetCaretopia.Chat.Controller;


import com.PetCaretopia.Chat.Model.Account;
import com.PetCaretopia.Chat.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/connect-chat")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private  PasswordEncoder passwordEncoder;



    @GetMapping("/connect")
    public String connect() {
        return "connect";
    }

    @GetMapping("/chat_room")
    public String chat_room( Model model) {

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            model.addAttribute("username", username);
            return "chat_room";

    }

    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public Account addUser(
            @Payload Account user
    ) {
        accountService.saveUser(user);
        return user;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public Account disconnectUser(
            @Payload Account user
    ) {
        accountService.disconnect(user);
        return user;
    }

    @GetMapping("/users")
    public ResponseEntity<List<Account>> findConnectedUsers() {
        return ResponseEntity.ok(accountService.findConnectedUsers());
    }
}
