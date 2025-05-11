package com.PetCaretopia.social.Service;

import com.PetCaretopia.shared.SharedImageUploadService;
import com.PetCaretopia.social.DTO.PostDTO;
import com.PetCaretopia.social.Mapper.PostMapper;
import com.PetCaretopia.social.entity.Post;
import com.PetCaretopia.social.entity.PostImage;
import com.PetCaretopia.social.repository.PostRepository;
import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PostMapper postMapper;
    @Autowired private SharedImageUploadService sharedImageUploadService;

    @Transactional
    public PostDTO createPostWithMultipart(PostDTO dto, List<MultipartFile> images) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow();
        Post post = postMapper.toEntity(dto, user);
        post.setUser(user);

        if (images != null && !images.isEmpty()) {
            List<PostImage> postImages = images.stream()
                    .map(file -> {
                        String url = sharedImageUploadService.uploadMultipartFile(file);
                        PostImage image = new PostImage();
                        image.setUrl(url);
                        image.setPost(post);
                        return image;
                    }).collect(Collectors.toList());
            post.setPostImages(postImages);
        }

        return postMapper.toDTO(postRepository.save(post));
    }

    public List<PostDTO> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PostDTO> getPostsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return postRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(postMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public PostDTO getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return postMapper.toDTO(post);
    }

    @Transactional
    public PostDTO updatePost(Long postId, PostDTO dto, List<MultipartFile> images) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setContent(dto.getContent());

        // Clear existing images
        if (post.getPostImages() != null) {
            post.getPostImages().clear();
        }

        if (images != null && !images.isEmpty()) {
            List<PostImage> postImages = images.stream()
                    .map(file -> {
                        String url = sharedImageUploadService.uploadMultipartFile(file);
                        PostImage image = new PostImage();
                        image.setUrl(url);
                        image.setPost(post);
                        return image;
                    }).collect(Collectors.toList());
            post.setPostImages(postImages);
        }

        return postMapper.toDTO(postRepository.save(post));
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        postRepository.delete(post);
    }

}
