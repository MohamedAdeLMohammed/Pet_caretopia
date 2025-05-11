package com.PetCaretopia.social.repository;

import com.PetCaretopia.social.entity.Comment;
import com.PetCaretopia.social.entity.Post;
import com.PetCaretopia.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByCreatedAtAsc(Post post);
    List<Comment> findByUser(User user);
    List<Comment> findByPostPostId(Long postPostId);
}