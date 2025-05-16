package com.PetCaretopia.social.Mapper;

import com.PetCaretopia.social.DTO.ReactionDTO;
import com.PetCaretopia.social.entity.Comment;
import com.PetCaretopia.social.entity.Reaction;
import com.PetCaretopia.social.entity.Post;
import com.PetCaretopia.user.DTO.UserSummaryDTO;
import com.PetCaretopia.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ReactionMapper {

    public Reaction toEntity(ReactionDTO dto, User user, Post post, Comment comment) {
        Reaction reaction = new Reaction();
        reaction.setUser(user);
        reaction.setPost(post);         // ممكن يكون null لو التفاعل على comment فقط
        reaction.setComment(comment);   // جديد
        reaction.setType(dto.getType());
        return reaction;
    }

    public ReactionDTO toDTO(Reaction reaction) {
        ReactionDTO dto = new ReactionDTO();
        dto.setReactionId(reaction.getReactionId());
        dto.setUserId(reaction.getUser().getUserID());

        if (reaction.getPost() != null)
            dto.setPostId(reaction.getPost().getPostId());

        if (reaction.getComment() != null)
            dto.setCommentId(reaction.getComment().getCommentId());

        dto.setType(reaction.getType());
        dto.setReactedAt(reaction.getReactedAt());

        // ✅ إضافة بيانات اليوزر
        User user = reaction.getUser();
        if (user != null) {
            UserSummaryDTO userDTO = new UserSummaryDTO();
            userDTO.setUserID(user.getUserID());
            userDTO.setUsername(user.getUsername());
            userDTO.setName(user.getName());
            userDTO.setProfileImageUrl(user.getUserProfileImage());
            dto.setUser(userDTO);
        }

        return dto;
    }

    public Reaction toPostEntity(ReactionDTO dto, User user, Post post) {
        Reaction reaction = new Reaction();
        reaction.setUser(user);
        reaction.setPost(post);
        reaction.setType(dto.getType());
        return reaction;
    }

    public Reaction toCommentEntity(ReactionDTO dto, User user, Comment comment) {
        Reaction reaction = new Reaction();
        reaction.setUser(user);
        reaction.setComment(comment);
        reaction.setType(dto.getType());
        return reaction;
    }

}
