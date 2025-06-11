package com.PetCaretopia.order.controller;



import com.PetCaretopia.order.DTO.ProductDTO;
import com.PetCaretopia.order.entity.ProductCategory;
import com.PetCaretopia.order.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private ProductService productService;


    @GetMapping("/products")
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }


    @GetMapping("/products/search")
    public List<ProductDTO> search(@RequestParam String q) {
        return productService.searchProducts(q);
    }


    @GetMapping("/products/category/{category}")
    public List<ProductDTO> getByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }


    @GetMapping("/products/available")
    public List<ProductDTO> availableProducts() {
        return productService.getAvailableProducts();
    }


    @GetMapping("/product/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @DeleteMapping("/product/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

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
