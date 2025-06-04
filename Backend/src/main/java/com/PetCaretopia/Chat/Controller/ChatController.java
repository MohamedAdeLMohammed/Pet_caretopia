package com.PetCaretopia.Chat.Controller;

import com.PetCaretopia.Chat.Model.Message;
import com.PetCaretopia.Chat.Service.MessageService;
import com.PetCaretopia.Notification.NotificationService;
import com.PetCaretopia.Notification.NotificationType;
import com.PetCaretopia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @MessageMapping("/chat")
    public void process(@Payload Message message) {
        Message savedMessage = messageService.save(message);

        notificationService.sendNotification(
                userRepository.findById(Long.valueOf(savedMessage.getReceiverId())).orElseThrow(),
                "You've received a new message!",
                NotificationType.GENERAL_ANNOUNCEMENT,
                savedMessage.getId(),
                "chat"
        );

        messagingTemplate.convertAndSendToUser(
                savedMessage.getReceiverId(), // still String — this is correct for WebSocket username
                "/queue/messages",
                savedMessage
        );
    }

    @GetMapping("/messages/{senderId}/{receiverId}")
    public ResponseEntity<List<Message>> getChatMessages(
            @PathVariable String senderId,
            @PathVariable String receiverId
    ) {
        var chatMessages = messageService.getChatMessage(senderId, receiverId);
        return ResponseEntity.ok(chatMessages);
    }
}
