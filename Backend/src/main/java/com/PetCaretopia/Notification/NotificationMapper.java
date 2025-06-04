package com.PetCaretopia.Notification;

import org.springframework.stereotype.Component;
@Component
public class NotificationMapper {

        public NotificationDTO toDTO(Notification notification) {
            NotificationDTO dto = new NotificationDTO();
            dto.setId(notification.getId());
            dto.setTitle(notification.getTitle());
            dto.setMessage(notification.getMessage());
            dto.setType(notification.getType());
            dto.setReferenceId(notification.getReferenceId());
            dto.setReferenceType(notification.getReferenceType());
            dto.setRead(notification.isRead());
            dto.setCreatedAt(notification.getCreatedAt());
            return dto;
        }
}

