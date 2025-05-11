package com.PetCaretopia.order.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderItemDTO {
    private Long id;

    private ProductDTO product;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @NotNull(message = "Product ID is required")
    private Long productId;
}
