package com.PetCaretopia.social.Service;

import com.PetCaretopia.social.DTO.NotificationDTO;
import com.PetCaretopia.social.Mapper.NotificationMapper;
import com.PetCaretopia.social.entity.Comment;
import com.PetCaretopia.social.entity.Post;
import com.PetCaretopia.social.entity.SocialNotification;
import com.PetCaretopia.social.repository.CommentRepository;
import com.PetCaretopia.social.repository.PostRepository;
import com.PetCaretopia.social.repository.SocialNotificationRepository;
import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private SocialNotificationRepository notificationRepository;
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    public NotificationDTO createNotification(NotificationDTO dto) {
        User recipient = userRepository.findById(dto.getRecipientId()).orElseThrow();
        User triggeredBy = userRepository.findById(dto.getTriggeredById()).orElseThrow();
        Post post = dto.getPostId() != null ? postRepository.findById(dto.getPostId()).orElse(null) : null;
        Comment comment = dto.getCommentId() != null ? commentRepository.findById(dto.getCommentId()).orElse(null) : null;

        SocialNotification notification = notificationMapper.toEntity(dto, recipient, triggeredBy, post, comment);
        return notificationMapper.toDTO(notificationRepository.save(notification));
    }

    public List<NotificationDTO> getUserNotifications(Long userId) {
        return notificationRepository.findByRecipient_UserIDOrderByCreatedAtDesc(userId)
                .stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        List<SocialNotification> unread = notificationRepository.findByRecipient_UserIDAndIsReadFalse(userId);
        unread.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(unread);
    }

    @Transactional
    public void markNotificationAsRead(Long notificationId) {
        SocialNotification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    public Long countUnread(Long userId) {
        return notificationRepository.countByRecipient_UserIDAndIsReadFalse(userId);
    }

}
