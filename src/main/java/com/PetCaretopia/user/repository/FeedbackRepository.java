package com.PetCaretopia.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.PetCaretopia.user.entity.Feedback;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByServiceProvider_ServiceProviderID(Long serviceProviderID);
    List<Feedback> findByUser_UserID(Long userID);
}
