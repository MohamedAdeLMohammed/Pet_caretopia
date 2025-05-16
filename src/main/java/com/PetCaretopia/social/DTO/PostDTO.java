package com.PetCaretopia.social.DTO;

import com.PetCaretopia.user.DTO.UserSummaryDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDTO {

    private Long postId;

    private UserSummaryDTO user;

    @NotBlank
    private String content;

    private List<PostImageDTO> postImages;

    @Transient
    @JsonIgnore
    private List<MultipartFile> postImageMultipartList;

    private LocalDateTime createdAt;

    // ✅ قائمة التعليقات المرتبطة بالبوست
    private List<CommentDTO> comments;

    // ✅ قائمة الريأكتس المرتبطة بالبوست
    private List<ReactionDTO> reactions;
}
