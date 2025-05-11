package com.PetCaretopia.order.mapper;

import com.PetCaretopia.order.DTO.WishlistItemDTO;
import com.PetCaretopia.order.entity.Product;
import com.PetCaretopia.order.entity.WishlistItem;
import org.springframework.stereotype.Component;

@Component
public class WishlistItemMapper {

    public WishlistItemDTO toDTO(WishlistItem entity) {
        WishlistItemDTO dto = new WishlistItemDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser().getUserID());
        dto.setProductId(entity.getProduct().getId());
        dto.setProductName(entity.getProduct().getName());

        if (entity.getProduct().getImages() != null && !entity.getProduct().getImages().isEmpty()) {
            dto.setProductImageUrl(entity.getProduct().getImages().get(0).getUrl());
        }

        return dto;
    }
}
