package com.PetCaretopia.user.DTO;

import lombok.Data;

@Data
public class UserSummaryDTO {
    private Long userID;
    private String name;
    private String username;
    private String profileImageUrl;
}
