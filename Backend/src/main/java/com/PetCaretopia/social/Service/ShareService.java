package com.PetCaretopia.social.Service;

import com.PetCaretopia.social.DTO.ShareDTO;
import com.PetCaretopia.social.entity.Post;
import com.PetCaretopia.social.entity.Share;
import com.PetCaretopia.social.Mapper.ShareMapper;
import com.PetCaretopia.social.repository.PostRepository;
import com.PetCaretopia.social.repository.ShareRepository;
import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShareService {

    @Autowired private ShareRepository shareRepository;
    @Autowired private ShareMapper shareMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private PostRepository postRepository;

    public ShareDTO sharePost(ShareDTO dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow();
        Post post = postRepository.findById(dto.getPostId()).orElseThrow();
        Share share = shareMapper.toEntity(dto, user, post);
        return shareMapper.toDTO(shareRepository.save(share));
    }

    public List<ShareDTO> getSharesByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return shareRepository.findByUser(user)
                .stream()
                .map(shareMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ShareDTO> getSharesByPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return shareRepository.findByPost(post)
                .stream()
                .map(shareMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteShare(Long shareId) {
        shareRepository.deleteById(shareId);
    }

    public boolean hasUserSharedPost(Long userId, Long postId) {
        return shareRepository.existsByUserUserIDAndPostPostId(userId, postId);
    }


    public List<ShareDTO> getRecentShares(Long userId, int limit) {
        User user = userRepository.findById(userId).orElseThrow();
        Pageable pageable = PageRequest.of(0, limit, Sort.by("sharedAt").descending());
        return shareRepository.findByUser(user, pageable)
                .stream()
                .map(shareMapper::toDTO)
                .collect(Collectors.toList());
    }


}