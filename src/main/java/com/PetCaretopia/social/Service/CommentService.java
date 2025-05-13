package com.PetCaretopia.social.Service;

import com.PetCaretopia.shared.SharedImageUploadService;
import com.PetCaretopia.social.DTO.CommentDTO;
import com.PetCaretopia.social.DTO.ReactionDTO;
import com.PetCaretopia.social.Mapper.CommentMapper;
import com.PetCaretopia.social.Mapper.ReactionMapper;
import com.PetCaretopia.social.entity.Comment;
import com.PetCaretopia.social.entity.CommentImage;
import com.PetCaretopia.social.entity.Post;
import com.PetCaretopia.social.entity.Reaction;
import com.PetCaretopia.social.repository.CommentRepository;
import com.PetCaretopia.social.repository.PostRepository;
import com.PetCaretopia.social.repository.ReactionRepository;
import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired private CommentRepository commentRepository;
    @Autowired private CommentMapper commentMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private SharedImageUploadService imageService;

    @Transactional
    public CommentDTO createCommentWithMultipart(CommentDTO dto, List<MultipartFile> images, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(dto.getPostId()).orElseThrow();

        Comment comment = commentMapper.toEntity(dto, user, post);

        if (images != null && !images.isEmpty()) {
            List<CommentImage> commentImages = images.stream().map(file -> {
                String url = imageService.uploadMultipartFile(file);
                CommentImage image = new CommentImage();
                image.setUrl(url);
                image.setComment(comment);
                return image;
            }).collect(Collectors.toList());
            comment.setCommentImages(commentImages);
        }

        return commentMapper.toDTO(commentRepository.save(comment));
    }

    public List<CommentDTO> getCommentsForPost(Long postId) {
        return commentRepository.findByPostPostId(postId).stream()
                .map(commentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CommentDTO getCommentById(Long commentId) {
        return commentMapper.toDTO(commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found")));
    }

    @Transactional
    public CommentDTO updateComment(Long commentId, CommentDTO dto, List<MultipartFile> images) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setContent(dto.getContent());

        comment.getCommentImages().clear(); // maintain reference
        if (images != null && !images.isEmpty()) {
            List<CommentImage> newImages = images.stream().map(file -> {
                String url = imageService.uploadMultipartFile(file);
                CommentImage image = new CommentImage();
                image.setUrl(url);
                image.setComment(comment);
                return image;
            }).collect(Collectors.toList());
            comment.getCommentImages().addAll(newImages);
        }

        return commentMapper.toDTO(commentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        commentRepository.delete(comment);
    }
}
