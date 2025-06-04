package com.PetCaretopia.Notification;

import com.PetCaretopia.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User recipient;

    private String title;
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private Long referenceId;       // e.g., Post ID or Appointment ID
    private String referenceType;   // e.g., "post", "appointment"

    private boolean isRead = false;
    private LocalDateTime createdAt = LocalDateTime.now();
}

