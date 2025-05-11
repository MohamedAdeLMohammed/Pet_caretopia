package com.PetCaretopia.social.repository;

import com.PetCaretopia.social.entity.Post;
import com.PetCaretopia.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserOrderByCreatedAtDesc(User user);
}