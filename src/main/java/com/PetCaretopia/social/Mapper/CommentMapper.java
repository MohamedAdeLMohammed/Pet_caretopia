package com.PetCaretopia.social.Mapper;

import com.PetCaretopia.social.DTO.CommentDTO;
import com.PetCaretopia.social.DTO.CommentImageDTO;
import com.PetCaretopia.social.entity.Comment;
import com.PetCaretopia.social.entity.CommentImage;
import com.PetCaretopia.social.entity.Post;
import com.PetCaretopia.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import com.PetCaretopia.social.DTO.ReactionDTO;
import com.PetCaretopia.social.entity.Reaction;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public Comment toEntity(CommentDTO dto, User user, Post post) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setUser(user);
        comment.setPost(post);
        return comment;
    }

    public CommentDTO toDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setCommentId(comment.getCommentId());
        dto.setContent(comment.getContent());
        dto.setPostId(comment.getPost().getPostId());
        dto.setCreatedAt(comment.getCreatedAt());

        // الصور
        if (comment.getCommentImages() != null) {
            List<CommentImageDTO> imageDTOs = comment.getCommentImages().stream().map(image -> {
                CommentImageDTO imageDTO = new CommentImageDTO();
                imageDTO.setId(image.getId());
                imageDTO.setUrl(image.getUrl());
                return imageDTO;
            }).collect(Collectors.toList());
            dto.setCommentImages(imageDTOs);
        }

        // ✅ الريأكشنات
        if (comment.getReactions() != null) {
            List<ReactionDTO> reactionDTOs = comment.getReactions().stream().map(reaction -> {
                ReactionDTO reactionDTO = new ReactionDTO();
                reactionDTO.setReactionId(reaction.getReactionId());
                reactionDTO.setUserId(reaction.getUser().getUserID());
                reactionDTO.setCommentId(comment.getCommentId()); // مهم
                reactionDTO.setType(reaction.getType());
                reactionDTO.setReactedAt(reaction.getReactedAt());
                return reactionDTO;
            }).collect(Collectors.toList());
            dto.setReactions(reactionDTOs);
        }

        return dto;
    }
}
