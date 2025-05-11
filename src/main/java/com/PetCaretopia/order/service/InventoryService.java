package com.PetCaretopia.order.service;


import com.PetCaretopia.order.DTO.ProductDTO;
import com.PetCaretopia.order.entity.Product;
import com.PetCaretopia.order.mapper.ProductMapper;
import com.PetCaretopia.order.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    public ProductDTO decreaseStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.decreaseStock(quantity);
        Product updatedProduct = productRepository.save(product);

        return productMapper.toDTO(updatedProduct);
    }

    public ProductDTO increaseStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.increaseStock(quantity);
        Product updatedProduct = productRepository.save(product);

        return productMapper.toDTO(updatedProduct);
    }
}
