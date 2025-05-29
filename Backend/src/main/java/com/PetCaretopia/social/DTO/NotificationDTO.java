package com.PetCaretopia.social.DTO;

import com.PetCaretopia.social.entity.NotificationType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long notificationId;

    @NotNull
    private Long recipientId;

    @NotNull
    private Long triggeredById;

    @NotNull
    private NotificationType type;

    private Long postId;
    private Long commentId;

    private Boolean isRead;
    private LocalDateTime createdAt;
}