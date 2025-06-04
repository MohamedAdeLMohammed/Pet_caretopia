package com.PetCaretopia.Notification;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class NotificationDTO {
    private Long id;
    private String title;
    private String message;
    private NotificationType type;
    private Long referenceId;
    private String referenceType;
    private boolean isRead;
    private LocalDateTime createdAt;
}
