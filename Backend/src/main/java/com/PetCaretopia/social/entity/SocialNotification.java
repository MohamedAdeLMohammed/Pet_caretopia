package com.PetCaretopia.social.entity;

import com.PetCaretopia.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class SocialNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient; // الشخص اللي هيجيله الإشعار

    @ManyToOne
    @JoinColumn(name = "triggered_by_id", nullable = false)
    private User triggeredBy; // الشخص اللي عمل Like أو Comment أو Share

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    // اختياري: لو الإشعار متعلق بـ Post
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    // اختياري: لو الإشعار متعلق بـ Comment
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Column(nullable = false)
    private Boolean isRead = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
