package com.PetCaretopia.user.controller;

import com.PetCaretopia.user.DTO.FeedbackDTO;
import com.PetCaretopia.user.entity.Feedback;
import com.PetCaretopia.user.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<FeedbackDTO> addFeedback(@RequestBody FeedbackDTO feedback) {
        return ResponseEntity.ok(feedbackService.saveFeedback(feedback));
    }


    @GetMapping("/serviceProvider/{id}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbackByServiceProvider(@PathVariable Long id) {
        return ResponseEntity.ok(feedbackService.getFeedbackByServiceProvider(id));
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbackByUser(@PathVariable Long id) {
        return ResponseEntity.ok(feedbackService.getFeedbackByUser(id));
    }
    @GetMapping("/{feedbackId}")
    public ResponseEntity<FeedbackDTO> getFeedbackById(@PathVariable Long feedbackId){
        return ResponseEntity.ok(feedbackService.getFeedbackById(feedbackId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<FeedbackDTO>> getAll(){
        return ResponseEntity.ok(feedbackService.getAllFeedbacks());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFeedback(@PathVariable Long id) {
        return ResponseEntity.ok(feedbackService.deleteFeedback(id));
    }
}
