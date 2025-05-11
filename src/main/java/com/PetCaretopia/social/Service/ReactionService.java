package com.PetCaretopia.social.Service;
import com.PetCaretopia.social.DTO.ReactionDTO;
import com.PetCaretopia.social.entity.Comment;
import com.PetCaretopia.social.entity.Post;
import com.PetCaretopia.social.entity.Reaction;
import com.PetCaretopia.social.Mapper.ReactionMapper;
import com.PetCaretopia.social.repository.CommentRepository;
import com.PetCaretopia.social.repository.PostRepository;
import com.PetCaretopia.social.repository.ReactionRepository;
import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReactionService {

    @Autowired private ReactionRepository reactionRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private CommentRepository commentRepository;
    @Autowired private ReactionMapper reactionMapper;

    @Transactional
    public ReactionDTO reactToPost(ReactionDTO dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow();
        Post post = postRepository.findById(dto.getPostId()).orElseThrow();
        reactionRepository.deleteByUserUserIDAndPostPostId(user.getUserID(), post.getPostId());
        Reaction reaction = reactionMapper.toPostEntity(dto, user, post);
        return reactionMapper.toDTO(reactionRepository.save(reaction));
    }

    @Transactional
    public ReactionDTO reactToComment(ReactionDTO dto, Long commentId) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow();
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        reactionRepository.deleteByUserUserIDAndCommentCommentId(user.getUserID(), commentId);
        Reaction reaction = reactionMapper.toCommentEntity(dto, user, comment);
        return reactionMapper.toDTO(reactionRepository.save(reaction));
    }

    public List<ReactionDTO> getReactionsByPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return reactionRepository.findByPost(post)
                .stream()
                .map(reactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ReactionDTO> getReactionsByComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        return reactionRepository.findByComment(comment)
                .stream()
                .map(reactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void removePostReaction(Long postId, Long userId) {
        reactionRepository.deleteByUserUserIDAndPostPostId(userId, postId);
    }

    public void removeCommentReaction(Long commentId, Long userId) {
        reactionRepository.deleteByUserUserIDAndCommentCommentId(userId, commentId);
    }

    public ReactionDTO getUserReaction(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        return reactionRepository.findByUserAndPost(user, post)
                .map(reactionMapper::toDTO)
                .orElse(null);
    }
    public ReactionDTO getUserReactionOnComment(Long commentId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        return reactionRepository.findByUserAndComment(user, comment)
                .map(reactionMapper::toDTO)
                .orElse(null);
    }


}
