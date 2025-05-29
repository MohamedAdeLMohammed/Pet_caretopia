package com.PetCaretopia.order.entity;

import jakarta.persistence.*;
import lombok.*;
import com.PetCaretopia.user.entity.User;


import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;


    public User getUser() {
        return user;
    }

}

