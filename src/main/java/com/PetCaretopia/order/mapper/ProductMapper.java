package com.PetCaretopia.order.mapper;


import com.PetCaretopia.order.DTO.ProductDTO;
import com.PetCaretopia.order.DTO.ProductImageDTO;
import com.PetCaretopia.order.entity.Product;
import com.PetCaretopia.order.entity.ProductImage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setCategory(product.getCategory());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());

        // تحويل الصور إلى DTOs وروابط URL فقط
        if (product.getImages() != null) {
            List<ProductImageDTO> imageDTOs = product.getImages().stream().map(image -> {
                ProductImageDTO imageDTO = new ProductImageDTO();
                imageDTO.setId(image.getId());
                imageDTO.setUrl(image.getUrl());
                return imageDTO;
            }).collect(Collectors.toList());

            List<String> imageUrls = imageDTOs.stream()
                    .map(ProductImageDTO::getUrl)
                    .collect(Collectors.toList());

            dto.setImages(imageDTOs);
            dto.setImageUrls(imageUrls);
        }

        return dto;
    }

    public Product toEntity(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setCategory(dto.getCategory());
        product.setCreatedAt(dto.getCreatedAt());
        product.setUpdatedAt(dto.getUpdatedAt());

        // لو فيه صور راجعة من الـ DTO، نربطها بالـ entity
        if (dto.getImages() != null) {
            List<ProductImage> images = dto.getImages().stream().map(imageDTO -> {
                ProductImage image = new ProductImage();
                image.setId(imageDTO.getId());
                image.setUrl(imageDTO.getUrl());
                image.setProduct(product); // ربط الصورة بالمنتج
                return image;
            }).collect(Collectors.toList());

            product.setImages(images);
        }

        return product;
    }
}
