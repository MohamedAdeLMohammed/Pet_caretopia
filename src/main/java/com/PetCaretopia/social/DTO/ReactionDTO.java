package com.PetCaretopia.social.DTO;

import com.PetCaretopia.social.entity.ReactionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReactionDTO {
    private Long reactionId;

    @NotNull
    private Long userId;

    private Long postId;    //  ممكن يكون null لو التفاعل على كومنت
    private Long commentId; //  لو التفاعل على كومنت

    @NotNull
    private ReactionType type;

    private LocalDateTime reactedAt;
}

