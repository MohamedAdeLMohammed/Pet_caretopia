package com.PetCaretopia.user.DTO;

import com.PetCaretopia.user.entity.ServiceProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class FeedbackDTO {
    private Long feedbackId;
    private Long userId;
    private Long serviceProviderId;
    private String userName;
    private String serviceProviderName;
    private String userEmail;
    private String serviceProviderEmail;
    private ServiceProvider.ServiceProviderType serviceProviderType;
    private String feedbackContent;
    private LocalDateTime createdAt;


}
