package com.PetCaretopia.social.repository;

import com.PetCaretopia.social.entity.Share;
import com.PetCaretopia.social.entity.Post;
import com.PetCaretopia.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareRepository extends JpaRepository<Share, Long> {
    List<Share> findByUser(User user);
    List<Share> findByPost(Post post);
    List<Share> findByUser(User user, Pageable pageable);
    boolean existsByUserUserIDAndPostPostId(Long userId, Long postId);


}
