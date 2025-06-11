package com.PetCaretopia.user.Mapper;

import com.PetCaretopia.Security.Util.GetAgeUtil;
import com.PetCaretopia.user.DTO.UserDTO;
import com.PetCaretopia.user.DTO.UserSummaryDTO;
import com.PetCaretopia.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final GetAgeUtil getAgeUtil;

    public UserMapper(GetAgeUtil getAgeUtil) {
        this.getAgeUtil = getAgeUtil;
    }

    public UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserID())
                .name(user.getName())
                .userEmail(user.getUserEmail())
                .userPhoneNumber(user.getUserPhoneNumber())
                .userAddress(user.getUserAddress())
                .userDetails(user.getUserDetails())
                .userImageProfile(user.getUserProfileImage())
                .birthDate(user.getBirthDate())
                .userGender(user.getUserGender())
                .userStatus(user.getUserStatus())
                .userAge(getAgeUtil.getAge(user.getBirthDate()))
                .build();
    }

    public UserSummaryDTO toUserSummaryDTO(User user){
        return new UserSummaryDTO(
                user.getUserID(),
                user.getName(),
                user.getUserEmail(),
                user.getUserProfileImage()
        );
    }
}
