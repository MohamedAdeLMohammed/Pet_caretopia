package com.PetCaretopia.user.service;


import com.PetCaretopia.user.entity.Feedback;
import com.PetCaretopia.user.repository.FeedbackRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getFeedbackByServiceProvider(Long serviceProviderID) {
        return feedbackRepository.findByServiceProvider_ServiceProviderID(serviceProviderID);
    }

    public List<Feedback> getFeedbackByUser(Long userID) {
        return feedbackRepository.findByUser_UserID(userID);
    }

    public Optional<Feedback> getFeedbackById(Long feedbackID) {
        return feedbackRepository.findById(feedbackID);
    }

    public void deleteFeedback(Long feedbackID) {
        feedbackRepository.deleteById(feedbackID);
    }
}
