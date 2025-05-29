package com.PetCaretopia.order.DTO;

import lombok.Data;

@Data
public class WishlistItemDTO {
    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private String productImageUrl;
}
