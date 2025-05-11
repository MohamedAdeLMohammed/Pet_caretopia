package com.PetCaretopia.order.mapper;


import com.PetCaretopia.order.DTO.OrderDTO;
import com.PetCaretopia.order.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    @Autowired
    private OrderItemMapper orderItemMapper;

    public OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getUserID());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        dto.setOrderItems(order.getOrderItems()
                .stream()
                .map(orderItemMapper::toDTO)
                .collect(Collectors.toList()));

        return dto;
    }
}
