package com.PetCaretopia.order.repository;

import com.PetCaretopia.order.entity.Product;
import com.PetCaretopia.order.entity.WishlistItem;
import com.PetCaretopia.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByUser(User user);
    boolean existsByUserAndProduct(User user, Product product);
    void deleteByUserAndProduct(User user, Product product);
    void deleteByUser(User user);
    int countByUser(User user);
}

