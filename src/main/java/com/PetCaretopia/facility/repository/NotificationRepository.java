package com.PetCaretopia.facility.repository;

import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.facility.entity.FacilityNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<FacilityNotification, Long> {

    List<FacilityNotification> findByUser(User user); // Get notifications by user

    List<FacilityNotification> findByIsRead(boolean isRead); // Get unread notifications
}
