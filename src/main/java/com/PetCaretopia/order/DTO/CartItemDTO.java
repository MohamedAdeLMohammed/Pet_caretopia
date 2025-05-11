package com.PetCaretopia.order.DTO;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CartItemDTO {
    private Long id;
    private ProductDTO product;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
