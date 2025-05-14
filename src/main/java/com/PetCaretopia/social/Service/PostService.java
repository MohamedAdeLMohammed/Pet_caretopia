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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PostMapper postMapper;
    @Autowired private SharedImageUploadService imageService;

    @Transactional
    public PostDTO createPostWithMultipart(PostDTO dto, List<MultipartFile> images, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Post post = postMapper.toEntity(dto, user);
        post.setUser(user);

        if (images != null && !images.isEmpty()) {
            List<PostImage> postImages = images.stream().map(file -> {
                String url = imageService.uploadMultipartFile(file);
                PostImage image = new PostImage();
                image.setUrl(url);
                image.setPost(post);
                return image;
            }
            ).collect(Collectors.toList());
            post.setPostImages(postImages);
            post.setUser(user);
        }
        return postMapper.toDTO(postRepository.save(post));
    }

    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(postMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PostDTO> getPostsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return postRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(postMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PostDTO getPostById(Long postId) {
        return postMapper.toDTO(
                postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"))
        );
    }

    @Transactional
    public PostDTO updatePost(Long postId, PostDTO dto, List<MultipartFile> images, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUser().getUserID().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot update this post.");
        }

        post.setContent(dto.getContent());
        post.getPostImages().clear();

        if (images != null && !images.isEmpty()) {
            List<PostImage> newImages = images.stream().map(file -> {
                String url = imageService.uploadMultipartFile(file);
                PostImage image = new PostImage();
                image.setUrl(url);
                image.setPost(post);
                return image;
            }).collect(Collectors.toList());
            post.getPostImages().addAll(newImages);
        }

        return postMapper.toDTO(postRepository.save(post));
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        if (!post.getUser().getUserID().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot delete this post.");
        }
        postRepository.delete(post);
    }
}
