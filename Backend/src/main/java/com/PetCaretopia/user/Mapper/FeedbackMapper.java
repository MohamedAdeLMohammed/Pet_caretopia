package com.PetCaretopia.user.mapper;

import com.PetCaretopia.user.DTO.FeedbackDTO;
import com.PetCaretopia.user.entity.Feedback;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {
    public FeedbackDTO toFeedbackDTO(Feedback feedback){
        return new FeedbackDTO(
                feedback.getFeedbackID(),
                feedback.getUser().getUserID(),
                feedback.getServiceProvider().getServiceProviderID(),
                feedback.getUser().getName(),
                feedback.getServiceProvider().getUser().getName(),
                feedback.getUser().getUserEmail(),
                feedback.getServiceProvider().getUser().getUserEmail(),
                feedback.getServiceProvider().getServiceProviderType(),
                feedback.getFeedbackContent(),
                feedback.getCreatedAt());
    }
}
