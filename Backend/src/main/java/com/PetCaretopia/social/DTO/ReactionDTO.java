package com.PetCaretopia.social.DTO;

import com.PetCaretopia.social.entity.ReactionType;
import com.PetCaretopia.user.DTO.UserSummaryDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReactionDTO {
    private Long reactionId;

    private Long userId;

    private Long postId;
    private Long commentId;

    @NotNull
    private ReactionType type;

    private LocalDateTime reactedAt;

    private UserSummaryDTO user;

}

