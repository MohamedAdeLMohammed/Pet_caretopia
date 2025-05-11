package com.PetCaretopia.social.Mapper;

import com.PetCaretopia.social.DTO.ShareDTO;
import com.PetCaretopia.social.entity.Post;
import com.PetCaretopia.social.entity.Share;
import com.PetCaretopia.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ShareMapper {

    public Share toEntity(ShareDTO dto, User user, Post post) {
        Share share = new Share();
        share.setUser(user);
        share.setPost(post);
        share.setShareMessage(dto.getShareMessage());
        return share;
    }

    public ShareDTO toDTO(Share share) {
        ShareDTO dto = new ShareDTO();
        dto.setShareId(share.getShareId());
        dto.setUserId(share.getUser().getUserID());
        dto.setPostId(share.getPost().getPostId());
        dto.setShareMessage(share.getShareMessage());
        dto.setSharedAt(share.getSharedAt());
        return dto;
    }
}

