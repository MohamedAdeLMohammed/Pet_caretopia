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
        if (product == null) return null;

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setCategory(product.getCategory());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());

        if (product.getImages() != null) {
            dto.setImages(product.getImages()
                    .stream()
                    .map(this::toImageDTO)
                    .collect(Collectors.toList()));

            dto.setImageUrls(
                    product.getImages().stream()
                            .map(ProductImage::getUrl)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    public Product toEntity(ProductDTO dto) {
        if (dto == null) return null;

        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setCategory(dto.getCategory());
        product.setCreatedAt(dto.getCreatedAt());
        product.setUpdatedAt(dto.getUpdatedAt());

        if (dto.getImages() != null) {
            List<ProductImage> images = dto.getImages().stream()
                    .map(this::toImageEntity)
                    .collect(Collectors.toList());

            for (ProductImage image : images) {
                image.setProduct(product);  // Set back-reference
            }

            product.setImages(images);
        }

        return product;
    }

    private ProductImageDTO toImageDTO(ProductImage image) {
        ProductImageDTO dto = new ProductImageDTO();
        dto.setId(image.getId());
        dto.setUrl(image.getUrl());
        return dto;
    }

    private ProductImage toImageEntity(ProductImageDTO dto) {
        ProductImage image = new ProductImage();
        image.setId(dto.getId());
        image.setUrl(dto.getUrl());
        return image;
    }
}
