package com.PetCaretopia.order.controller;


import jakarta.validation.Valid;

import com.PetCaretopia.order.DTO.ProductDTO;
import com.PetCaretopia.order.entity.ProductCategory;
import com.PetCaretopia.order.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private ProductService productService;

    //  Public: view all products
    @PreAuthorize("permitAll()")
    @GetMapping("/products")
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    //  Public: search products
    @PreAuthorize("permitAll()")
    @GetMapping("/products/search")
    public List<ProductDTO> search(@RequestParam String q) {
        return productService.searchProducts(q);
    }

    //  Public: filter by category
    @PreAuthorize("permitAll()")
    @GetMapping("/products/category/{category}")
    public List<ProductDTO> getByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }

    //  Public: check available products
    @PreAuthorize("permitAll()")
    @GetMapping("/products/available")
    public List<ProductDTO> availableProducts() {
        return productService.getAvailableProducts();
    }


    //  Public: product details
    @PreAuthorize("permitAll()")
    @GetMapping("/product/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    //  Admin only: delete product
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/product/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    //  Public: search with optional category filter
    @PreAuthorize("permitAll()")
    @GetMapping("/products/search-filter")
    public List<ProductDTO> searchAndFilter(@RequestParam String keyword,
                                            @RequestParam(required = false) String category) {
        ProductCategory cat = null;
        if (category != null) {
            try {
                cat = ProductCategory.valueOf(category.toUpperCase());
            } catch (Exception e) {
                throw new RuntimeException("Invalid category name: " + category);
            }
        }
        return productService.searchAndFilter(keyword, cat);
    }
}
