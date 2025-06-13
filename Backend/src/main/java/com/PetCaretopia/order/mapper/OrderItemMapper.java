package com.PetCaretopia.order.mapper;

import com.PetCaretopia.order.DTO.OrderItemDTO;
import com.PetCaretopia.order.entity.OrderItem;
import com.PetCaretopia.order.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    @Autowired
    private ProductMapper productMapper;

    public OrderItemDTO toDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());

        Product product = item.getProduct();
        if (product != null) {
            dto.setProductId(product.getId());
            dto.setProduct(productMapper.toDTO(product));  // 🟢 Map full product
        }

        return dto;
    }
}
