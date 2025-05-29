package com.PetCaretopia.social.DTO;

import com.PetCaretopia.user.DTO.UserSummaryDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentDTO {
    private Long commentId;

    private UserSummaryDTO user;

    @NotNull
    private Long postId;

    @NotBlank
    private String content;

    private List<CommentImageDTO> commentImages;
    private List<ReactionDTO> reactions;
    private LocalDateTime createdAt;
}
