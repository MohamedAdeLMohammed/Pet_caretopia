package com.PetCaretopia.social.DTO;

import com.PetCaretopia.social.entity.ReportStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportDTO {
    private Long reportId;

    @NotNull
    private Long reporterId;

    private Long reportedUserId;
    private Long reportedPostId;
    private Long reportedCommentId;

    @NotBlank
    private String reportReason;


    private ReportStatus status;     // PENDING / RESOLVED / DISMISSED
    private boolean isArchived;

    private LocalDateTime createdAt;
}
