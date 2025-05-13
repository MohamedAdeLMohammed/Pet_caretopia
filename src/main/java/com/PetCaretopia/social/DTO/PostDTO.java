package com.PetCaretopia.social.DTO;

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

    @NotBlank
    private String content;

    private List<PostImageDTO> postImages;

    @Transient
    @JsonIgnore
    private List<MultipartFile> postImageMultipartList;


    private LocalDateTime createdAt;
}
