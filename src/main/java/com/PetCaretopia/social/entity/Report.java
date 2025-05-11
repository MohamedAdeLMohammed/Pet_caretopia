package com.PetCaretopia.social.entity;

import com.PetCaretopia.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Getter
@Setter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "reported_user_id")
    private User reportedUser;

    @ManyToOne
    @JoinColumn(name = "reported_post_id")
    private Post reportedPost;

    @ManyToOne
    @JoinColumn(name = "reported_comment_id")
    private Comment reportedComment;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reportReason;

    // ✅ حالة البلاغ: PENDING / RESOLVED / DISMISSED
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.PENDING;

    // ✅ أرشفة (اختياري)
    @Column(nullable = false)
    private boolean isArchived = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

