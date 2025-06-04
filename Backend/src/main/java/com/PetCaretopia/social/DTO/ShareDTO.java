package com.PetCaretopia.social.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShareDTO {
    private Long shareId;

    private Long userId;

    @NotNull
    private Long postId;

    private String shareMessage;

    private LocalDateTime sharedAt;
}