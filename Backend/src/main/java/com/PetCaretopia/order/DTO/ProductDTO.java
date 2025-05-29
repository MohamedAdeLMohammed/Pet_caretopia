package com.PetCaretopia.order.DTO;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.PetCaretopia.order.entity.ProductCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductDTO {

    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity must be zero or more")
    private Integer stockQuantity;

    @NotNull(message = "Category is required")
    private ProductCategory category;

    private List<String> imageUrls;            // ✅ للاستخدام في العرض
    private List<ProductImageDTO> images;      // ✅ تفاصيل الصور المرتبطة

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
