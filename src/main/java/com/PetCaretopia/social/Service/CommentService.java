package com.PetCaretopia.social.Service;

import com.PetCaretopia.shared.SharedImageUploadService;
import com.PetCaretopia.social.DTO.CommentDTO;
import com.PetCaretopia.social.DTO.ReactionDTO;
import com.PetCaretopia.social.Mapper.CommentMapper;
import com.PetCaretopia.social.Mapper.ReactionMapper;
import com.PetCaretopia.social.entity.Comment;
import com.PetCaretopia.social.entity.CommentImage;
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
    @Autowired private SharedImageUploadService sharedImageUploadService;
    @Autowired private ReactionRepository reactionRepository;
    @Autowired private ReactionMapper reactionMapper;

    @Transactional
    public CommentDTO createCommentWithMultipart(CommentDTO dto, List<MultipartFile> images) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow();
        Comment comment = commentMapper.toEntity(dto, user, postRepository.findById(dto.getPostId()).orElseThrow());

        if (images != null && !images.isEmpty()) {
            List<CommentImage> commentImages = images.stream()
                    .map(file -> {
                        String url = sharedImageUploadService.uploadMultipartFile(file);
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
        return commentRepository.findByPostPostId(postId)
                .stream()
                .map(commentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDTO updateComment(Long commentId, CommentDTO dto, List<MultipartFile> newImages) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        existingComment.setContent(dto.getContent());

        if (existingComment.getCommentImages() != null) {
            existingComment.getCommentImages().clear();
        }

        if (newImages != null && !newImages.isEmpty()) {
            List<CommentImage> uploadedImages = newImages.stream()
                    .map(file -> {
                        String url = sharedImageUploadService.uploadMultipartFile(file);
                        CommentImage image = new CommentImage();
                        image.setUrl(url);
                        image.setComment(existingComment);
                        return image;
                    }).collect(Collectors.toList());
            existingComment.setCommentImages(uploadedImages);
        }

        return commentMapper.toDTO(commentRepository.save(existingComment));
    }

    public CommentDTO getCommentById(Long commentId) {
        return commentMapper.toDTO(commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found")));
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.delete(commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found")));
    }

    // ✅ Reaction on comment
    @Transactional
    public ReactionDTO reactToComment(ReactionDTO dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow();
        Comment comment = commentRepository.findById(dto.getCommentId()).orElseThrow();

        reactionRepository.deleteByUserUserIDAndCommentCommentId(user.getUserID(), comment.getCommentId());

        Reaction reaction = new Reaction();
        reaction.setUser(user);
        reaction.setComment(comment);
        reaction.setType(dto.getType());

        return reactionMapper.toDTO(reactionRepository.save(reaction));
    }

    public List<ReactionDTO> getReactionsByComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        return reactionRepository.findByComment(comment)
                .stream()
                .map(reactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ReactionDTO getUserCommentReaction(Long commentId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        return reactionRepository.findByUserAndComment(user, comment)
                .map(reactionMapper::toDTO)
                .orElse(null);
    }

    @Transactional
    public void removeCommentReaction(Long commentId, Long userId) {
        reactionRepository.deleteByUserUserIDAndCommentCommentId(userId, commentId);
    }
}
