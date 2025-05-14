package com.PetCaretopia.social.Mapper;

import com.PetCaretopia.shared.SharedImageUploadService;
import com.PetCaretopia.social.DTO.PostDTO;
import com.PetCaretopia.social.DTO.PostImageDTO;
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

        // Map user -> UserSummaryDTO
        User user = post.getUser();
        if (user != null) {
            UserSummaryDTO userDTO = new UserSummaryDTO();
            userDTO.setUserID(user.getUserID());
            userDTO.setUsername(user.getUsername());
            userDTO.setProfileImageUrl(user.getUserProfileImage()); // تأكد أن ده موجود في كيان User
            dto.setUser(userDTO);
        }

        // Map images
        if (post.getPostImages() != null) {
            List<PostImageDTO> images = post.getPostImages().stream()
                    .map(image -> {
                        PostImageDTO imageDTO = new PostImageDTO();
                        imageDTO.setId(image.getId());
                        imageDTO.setUrl(image.getUrl());
                        return imageDTO;
                    }).collect(Collectors.toList());

            dto.setPostImages(images);
        }

        return dto;
    }
}

