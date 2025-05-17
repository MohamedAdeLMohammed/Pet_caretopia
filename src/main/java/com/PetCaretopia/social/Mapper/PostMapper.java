package com.PetCaretopia.social.Mapper;

import com.PetCaretopia.shared.SharedImageUploadService;
import com.PetCaretopia.social.DTO.*;
import com.PetCaretopia.social.entity.Post;
import com.PetCaretopia.social.entity.PostImage;
import com.PetCaretopia.user.DTO.UserSummaryDTO;
import com.PetCaretopia.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component

public class PostMapper {


    private final CommentMapper commentMapper;
    private final ReactionMapper reactionMapper;
    private final ShareMapper shareMapper;

    @Autowired
    public PostMapper(CommentMapper commentMapper,
                      ReactionMapper reactionMapper,
                      ShareMapper shareMapper) {
        this.commentMapper = commentMapper;
        this.reactionMapper = reactionMapper;
        this.shareMapper = shareMapper;
    }


    public Post toEntity(PostDTO dto, User user) {
        Post post = new Post();
        post.setContent(dto.getContent());
        post.setUser(user);
        // الصور هتتحط في السيرفيس مباشرة
        return post;
    }

    public PostDTO toDTO(Post post) {
        PostDTO dto = new PostDTO();
        dto.setPostId(post.getPostId());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());

        // 🟢 الماب الخاصة باليوزر
        User user = post.getUser();
        if (user != null) {
            UserSummaryDTO userDTO = new UserSummaryDTO();
            userDTO.setUserID(user.getUserID());
            userDTO.setUsername(user.getUsername());
            userDTO.setName(user.getName());
            userDTO.setProfileImageUrl(user.getUserProfileImage());
            dto.setUser(userDTO);
        }

        // 🟢 ماب الصور
        if (post.getPostImages() != null) {
            List<PostImageDTO> images = post.getPostImages().stream()
                    .map(image -> {
                        PostImageDTO imageDTO = new PostImageDTO();
                        imageDTO.setId(image.getId());
                        imageDTO.setUrl(image.getUrl());
                        return imageDTO;
                    }).collect(Collectors.toList());
            dto.setPostImages(images);
        } else {
            dto.setPostImages(List.of());
        }

        // 🟢 ماب التعليقات
        if (post.getComments() != null) {
            List<CommentDTO> commentDTOs = post.getComments().stream()
                    .map(commentMapper::toDTO)
                    .collect(Collectors.toList());
            dto.setComments(commentDTOs);
        } else {
            dto.setComments(List.of());
        }

        // 🟢 ماب الريأكتس
        if (post.getReactions() != null) {
            List<ReactionDTO> reactionDTOs = post.getReactions().stream()
                    .map(reactionMapper::toDTO)
                    .collect(Collectors.toList());
            dto.setReactions(reactionDTOs);
        } else {
            dto.setReactions(List.of());
        }

        // 🟢 ماب المشاركات
        if (post.getShares() != null) {
            List<ShareDTO> shareDTOs = post.getShares().stream()
                    .map(shareMapper::toDTO)
                    .collect(Collectors.toList());
            dto.setShares(shareDTOs);
        } else {
            dto.setShares(List.of());
        }

        return dto;
    }

}

