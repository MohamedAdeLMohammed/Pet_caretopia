package com.PetCaretopia.social.Mapper;

import com.PetCaretopia.social.DTO.NotificationDTO;
import com.PetCaretopia.social.entity.Comment;
import com.PetCaretopia.social.entity.NotificationType;
import com.PetCaretopia.social.entity.Post;
import com.PetCaretopia.social.entity.SocialNotification;
import com.PetCaretopia.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public SocialNotification toEntity(NotificationDTO dto, User recipient, User triggeredBy, Post post, Comment comment) {
        SocialNotification notification = new SocialNotification();
        notification.setRecipient(recipient);
        notification.setTriggeredBy(triggeredBy);
        notification.setType(dto.getType());
        notification.setPost(post);
        notification.setComment(comment);
        notification.setIsRead(dto.getIsRead() != null ? dto.getIsRead() : false);
        return notification;
    }

    public NotificationDTO toDTO(SocialNotification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setNotificationId(notification.getNotificationId());
        dto.setRecipientId(notification.getRecipient().getUserID());
        dto.setTriggeredById(notification.getTriggeredBy().getUserID());
        dto.setType(notification.getType());
        if (notification.getPost() != null) dto.setPostId(notification.getPost().getPostId());
        if (notification.getComment() != null) dto.setCommentId(notification.getComment().getCommentId());
        dto.setIsRead(notification.getIsRead());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }
}
