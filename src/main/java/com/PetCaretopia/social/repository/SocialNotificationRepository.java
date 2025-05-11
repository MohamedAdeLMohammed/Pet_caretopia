package com.PetCaretopia.social.repository;

import com.PetCaretopia.social.entity.SocialNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocialNotificationRepository extends JpaRepository<SocialNotification, Long> {

    // Get all notifications for user ordered by newest
    List<SocialNotification> findByRecipient_UserIDOrderByCreatedAtDesc(Long userId);

    // Get unread notifications only
    List<SocialNotification> findByRecipient_UserIDAndIsReadFalse(Long userId);

    // Count of unread notifications
    Long countByRecipient_UserIDAndIsReadFalse(Long userId);
}
