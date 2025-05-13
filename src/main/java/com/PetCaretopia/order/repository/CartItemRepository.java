package com.PetCaretopia.order.repository;


import com.PetCaretopia.order.entity.Cart;
import com.PetCaretopia.order.entity.CartItem;
import com.PetCaretopia.order.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCart(Cart cart);

    void deleteByCart(Cart cart); // 🔑 For clearing cart

    List<CartItem> findByProduct(Product product);

}
