package com.PetCaretopia.user.service;


import com.PetCaretopia.user.DTO.FeedbackDTO;
import com.PetCaretopia.user.entity.Feedback;
import com.PetCaretopia.user.mapper.FeedbackMapper;
import com.PetCaretopia.user.repository.FeedbackRepository;
import com.PetCaretopia.user.repository.ServiceProviderRepository;
import com.PetCaretopia.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final FeedbackMapper feedbackMapper;

    public FeedbackService(FeedbackRepository feedbackRepository, UserRepository userRepository, ServiceProviderRepository serviceProviderRepository, FeedbackMapper feedbackMapper) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.serviceProviderRepository = serviceProviderRepository;
        this.feedbackMapper = feedbackMapper;
    }

    public FeedbackDTO saveFeedback(FeedbackDTO dto) {
        var feedbackUser = userRepository.findById(dto.getUserId()).orElseThrow(()->new IllegalArgumentException("User Not Found !"));
        var feedbackServiceProvider = serviceProviderRepository.findById(dto.getServiceProviderId()).orElseThrow(()->new IllegalArgumentException("Service Provider Not Found !"));
        Feedback feedback = Feedback.builder()
                .user(feedbackUser)
                .serviceProvider(feedbackServiceProvider)
                .feedbackContent(dto.getFeedbackContent())
                .createdAt(LocalDateTime.now())
                .build();
        feedbackRepository.save(feedback);
        return feedbackMapper.toFeedbackDTO(feedback);


    }

    public List<FeedbackDTO> getFeedbackByServiceProvider(Long serviceProviderID) {
        var feedbacks = feedbackRepository.findByServiceProvider_ServiceProviderID(serviceProviderID);
        return feedbacks.stream().map(feedbackMapper::toFeedbackDTO).collect(Collectors.toList());
    }

    public List<FeedbackDTO> getFeedbackByUser(Long userID) {

       var feedbacks = feedbackRepository.findByUser_UserID(userID);
        return feedbacks.stream().map(feedbackMapper::toFeedbackDTO).collect(Collectors.toList());
    }

    public FeedbackDTO getFeedbackById(Long feedbackID) {
        var feedback = feedbackRepository.findById(feedbackID).orElseThrow(()->new IllegalArgumentException("Feedback Not Found !"));
        return feedbackMapper.toFeedbackDTO(feedback);
    }

    public String deleteFeedbackByUserIdAndFeedbackId(Long feedbackID,Long userID) {
        var feedback = feedbackRepository.findById(feedbackID)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found!"));

        if (!feedback.getUser().getUserID().equals(userID)) {
            throw new IllegalArgumentException("Feedback does not belong to the given user!");
        }
        feedbackRepository.deleteById(feedbackID);
        return "Feedback Deleted !";
    }
    public List<FeedbackDTO> getAllFeedbacks(){
        var feedbacks = feedbackRepository.findAll();
        return feedbacks.stream().map(feedbackMapper::toFeedbackDTO).collect(Collectors.toList());
    }
    public FeedbackDTO updateFeedbackByUserIdAndFeedbackId(Long userId, Long feedbackId, String newContent) {
        var feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found!"));

        if (!feedback.getUser().getUserID().equals(userId)) {
            throw new IllegalArgumentException("Feedback does not belong to the given user!");
        }

        if (newContent != null && !newContent.isEmpty()) {
            feedback.setFeedbackContent(newContent);
        }

        feedbackRepository.save(feedback);
        return feedbackMapper.toFeedbackDTO(feedback);
    }
}
