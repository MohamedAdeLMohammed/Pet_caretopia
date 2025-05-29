package com.PetCaretopia.order.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.PetCaretopia.order.entity.OrderStatus;


@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    private BigDecimal totalPrice;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemDTO> orderItems;
}
