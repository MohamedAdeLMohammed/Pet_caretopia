package com.PetCaretopia.social.entity;

import com.PetCaretopia.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reactions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "post_id"}),
        @UniqueConstraint(columnNames = {"user_id", "comment_id"})
})
@Getter
@Setter
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactionId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReactionType type;

    @Column(nullable = false, updatable = false)
    private LocalDateTime reactedAt;


    @PrePersist
    protected void onReact() {
        this.reactedAt = LocalDateTime.now();
    }
}
