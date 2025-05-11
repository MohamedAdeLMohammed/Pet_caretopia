package com.PetCaretopia.order.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CartDTO {

    private Long cartId;
    private Long userId;
    private List<CartItemDTO> cartItems;

}
