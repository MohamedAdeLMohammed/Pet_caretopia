package com.PetCaretopia.order.mapper;


import com.PetCaretopia.order.DTO.OrderItemDTO;
import com.PetCaretopia.order.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    public OrderItemDTO toDTO(OrderItem orderItem) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(orderItem.getId());
        dto.setProductId(orderItem.getProduct().getId());
        dto.setPrice(orderItem.getPrice());
        dto.setQuantity(orderItem.getQuantity());
        dto.setCreatedAt(orderItem.getCreatedAt());
        dto.setUpdatedAt(orderItem.getUpdatedAt());
        return dto;
    }
}
