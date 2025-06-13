package com.PetCaretopia.order.repository;


import com.PetCaretopia.order.entity.Cart;
import com.PetCaretopia.order.entity.CartItem;
import com.PetCaretopia.order.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product WHERE ci.cart = :cart")
    List<CartItem> findByCart(@Param("cart") Cart cart);
    void deleteByCart(Cart cart); // 🔑 For clearing cart

    List<CartItem> findByProduct(Product product);

}
