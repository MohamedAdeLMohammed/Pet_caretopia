package com.PetCaretopia.user.mapper;

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

    public UserDTO toUserDTO(User user){
        return new UserDTO(
                user.getName(),
                user.getUserEmail(),
                user.getUserPhoneNumber(),
                user.getUserAddress(),
                user.getUserDetails(),
                user.getUserProfileImage(),
                user.getBirthDate(),
                user.getUserGender(),
                user.getUserStatus(),
                getAgeUtil.getAge(user.getBirthDate()));
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
