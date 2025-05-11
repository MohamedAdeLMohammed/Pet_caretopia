package com.PetCaretopia.social.controller;

import com.PetCaretopia.social.DTO.NotificationDTO;
import com.PetCaretopia.social.Service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/social/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @GetMapping("/user/{userId}/unread-count")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    public ResponseEntity<Long> getUnreadNotificationCount(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.countUnread(userId));
    }

    @PatchMapping("/user/{userId}/read")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{notificationId}/read")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{notificationId}")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    public ResponseEntity<NotificationDTO> createNotification(@Valid @RequestBody NotificationDTO dto) {
        return ResponseEntity.ok(notificationService.createNotification(dto));
    }
}
