package com.PetCaretopia.user.DTO;

import com.PetCaretopia.user.entity.User;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
  private String name;
  private String userEmail;
  private String userPhoneNumber;
  private String userAddress;
  private String userDetails;
  private String userImageProfile;
  private LocalDate birthDate;
  private User.Gender userGender;
  private User.Status userStatus;
  private String userAge;
}
