package com.PetCaretopia.order.controller;


import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.PetCaretopia.order.DTO.ProductDTO;
import com.PetCaretopia.order.entity.ProductCategory;
import com.PetCaretopia.order.service.ProductService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    //  Public: Get all products
    @PreAuthorize("permitAll()")
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    //  Public: Get product by ID
    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    //  Admin: Create product (accepts Base64 images)
    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")

    public ProductDTO addProduct(
            @RequestPart("product") @Valid ProductDTO productDTO,
            @RequestPart("images") List<MultipartFile> images) {
        return productService.saveProductWithMultipart(productDTO, images);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductDTO updateProduct(@PathVariable Long id,
                                    @RequestPart("product") @Valid ProductDTO updatedProductDTO,
                                    @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        updatedProductDTO.setId(id);
        return productService.saveProductWithMultipart(updatedProductDTO, images);
    }

    //  Admin: Delete product
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    //  Public: Search products by keyword
    @PreAuthorize("permitAll()")
    @GetMapping("/search")
    public List<ProductDTO> searchProducts(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }

    //  Public: Filter by category (strict enum)
    @PreAuthorize("permitAll()")
    @GetMapping("/category")
    public List<ProductDTO> getByCategory(@RequestParam ProductCategory category) {
        return productService.filterByCategory(category);
    }

    //  Public: Search with optional category filter
    @PreAuthorize("permitAll()")
    @GetMapping("/search-filter")
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
