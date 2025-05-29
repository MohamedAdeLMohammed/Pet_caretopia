package com.PetCaretopia.social.repository;

import com.PetCaretopia.social.entity.Comment;
import com.PetCaretopia.social.entity.Reaction;
import com.PetCaretopia.social.entity.Post;
import com.PetCaretopia.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    Optional<Reaction> findByUserAndPost(User user, Post post);
    List<Reaction> findByPost(Post post);
    boolean existsByUserUserIDAndPostPostId(Long userID, Long postId);
    void deleteByUserUserIDAndPostPostId(Long userID, Long postId);


    Optional<Reaction> findByUserAndComment(User user, Comment comment);
    List<Reaction> findByComment(Comment comment);
    boolean existsByUserUserIDAndCommentCommentId(Long userID, Long commentId);
    void deleteByUserUserIDAndCommentCommentId(Long userID, Long commentId);
}
