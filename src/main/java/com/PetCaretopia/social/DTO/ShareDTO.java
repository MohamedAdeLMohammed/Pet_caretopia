package com.PetCaretopia.social.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShareDTO {
    private Long shareId;

    @NotNull
    private Long userId;

    @NotNull
    private Long postId;

    private String shareMessage;
    private LocalDateTime sharedAt;
}