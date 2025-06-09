package com.PetCaretopia.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryDTO {
    private Long userID;
    private String name;
    private String username;
    private String profileImageUrl;
}
