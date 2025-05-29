package com.PetCaretopia.Security.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordRequest {
    private String email;
    private String phoneNumber;
    private String newPassword;
    private String confirmNewPassword;
}
