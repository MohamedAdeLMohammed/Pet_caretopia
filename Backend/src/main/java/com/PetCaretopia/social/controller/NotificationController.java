package com.PetCaretopia.social.controller;

import com.PetCaretopia.Security.Service.CustomUserDetails;
import com.PetCaretopia.social.DTO.NotificationDTO;
import com.PetCaretopia.social.Service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/social/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(@AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(notificationService.getUserNotifications(principal.getUserId()));
    }

    @GetMapping("/user/unread-count")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    public ResponseEntity<Long> getUnreadNotificationCount(@AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(notificationService.countUnread(principal.getUserId()));
    }

    @PatchMapping("/user/read")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    public ResponseEntity<Void> markAllAsRead(@AuthenticationPrincipal CustomUserDetails principal) {
        notificationService.markAllAsRead(principal.getUserId());
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
