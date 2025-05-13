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
    @Autowired private ReactionMapper reactionMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private CommentRepository commentRepository;

    @Transactional
    public ReactionDTO reactToPost(ReactionDTO dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow();
        Post post = postRepository.findById(dto.getPostId()).orElseThrow();

        reactionRepository.deleteByUserUserIDAndPostPostId(user.getUserID(), post.getPostId());

        Reaction reaction = new Reaction();
        reaction.setUser(user);
        reaction.setPost(post);
        reaction.setType(dto.getType());

        return reactionMapper.toDTO(reactionRepository.save(reaction));
    }

    @Transactional
    public ReactionDTO reactToComment(ReactionDTO dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow();
        Comment comment = commentRepository.findById(dto.getCommentId()).orElseThrow();

        reactionRepository.deleteByUserUserIDAndCommentCommentId(user.getUserID(), comment.getCommentId());

        Reaction reaction = new Reaction();
        reaction.setUser(user);
        reaction.setComment(comment);
        reaction.setPost(comment.getPost()); // fix foreign key
        reaction.setType(dto.getType());

        return reactionMapper.toDTO(reactionRepository.save(reaction));
    }

    public List<ReactionDTO> getReactionsByPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return reactionRepository.findByPost(post).stream()
                .map(reactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ReactionDTO> getReactionsByComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        return reactionRepository.findByComment(comment).stream()
                .map(reactionMapper::toDTO)
                .collect(Collectors.toList());
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

    @Transactional
    public void removeReactionFromPost(Long postId, Long userId) {
        reactionRepository.deleteByUserUserIDAndPostPostId(userId, postId);
    }

    @Transactional
    public void removeReactionFromComment(Long commentId, Long userId) {
        reactionRepository.deleteByUserUserIDAndCommentCommentId(userId, commentId);
    }
}
