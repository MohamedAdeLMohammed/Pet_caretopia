package com.PetCaretopia.social.entity;

import com.PetCaretopia.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "shares")
@Getter
@Setter
public class Share {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "share_id")
    private Long shareId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private String shareMessage;

    @Column(nullable = false, updatable = false)
    private LocalDateTime sharedAt;

    @PrePersist
    protected void onShare() {
        this.sharedAt = LocalDateTime.now();
    }
}
