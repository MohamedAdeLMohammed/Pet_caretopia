package com.PetCaretopia.user.DTO;

import com.PetCaretopia.user.entity.ServiceProvider;
import com.PetCaretopia.user.entity.User;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceProviderSimpleDTO {
    private Long userId;
    private Long serviceProviderId;
    private String name;
    private String userEmail;
    private String userPhoneNumber;
    private String userAddress;
    private User.Gender userGender;
    private User.Status userStatus;
    private String userAge;
    private LocalDate birthDate;
    private String userDetails;
    private String userProfileImage;
    private BigDecimal serviceProviderSalary;
    private ServiceProvider.ServiceProviderType serviceProviderType;
    private Integer serviceProviderExperience;
}
