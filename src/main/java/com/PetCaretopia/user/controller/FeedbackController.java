package com.PetCaretopia.user.controller;

import com.PetCaretopia.user.entity.Feedback;
import com.PetCaretopia.user.service.FeedbackService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/add")
    public Feedback addFeedback(@RequestBody Feedback feedback) {
        return feedbackService.saveFeedback(feedback);
    }

    @GetMapping("/serviceProvider/{id}")
    public List<Feedback> getFeedbackByServiceProvider(@PathVariable Long id) {
        return feedbackService.getFeedbackByServiceProvider(id);
    }

    @GetMapping("/user/{id}")
    public List<Feedback> getFeedbackByUser(@PathVariable Long id) {
        return feedbackService.getFeedbackByUser(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
    }
}
