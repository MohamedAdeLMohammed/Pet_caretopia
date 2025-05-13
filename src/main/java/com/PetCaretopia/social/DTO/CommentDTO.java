package com.PetCaretopia.social.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentDTO {
    private Long commentId;



    @NotNull
    private Long postId;

    @NotBlank
    private String content;

    private List<CommentImageDTO> commentImages;
    private List<ReactionDTO> reactions;
    private LocalDateTime createdAt;
}
