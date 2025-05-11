package com.PetCaretopia.order.service;


import com.PetCaretopia.order.DTO.ProductDTO;
import com.PetCaretopia.order.entity.Product;
import com.PetCaretopia.order.entity.ProductCategory;
import com.PetCaretopia.order.entity.ProductImage;
import com.PetCaretopia.order.mapper.ProductMapper;
import com.PetCaretopia.order.repository.ProductRepository;
import com.PetCaretopia.shared.SharedImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private SharedImageUploadService sharedImageUploadService;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByCategory(String categoryName) {
        try {
            ProductCategory category = ProductCategory.valueOf(categoryName.toUpperCase());
            return productRepository.findByCategory(category)
                    .stream()
                    .map(productMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid category name: " + categoryName);
        }
    }

    public List<ProductDTO> getAvailableProducts() {
        return productRepository.findAvailableProducts()
                .stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }



//    public ProductDTO saveProduct(ProductDTO productDTO) {
//        Product product = productMapper.toEntity(productDTO);
//
//        // رفع الصور (Base64 → URL) ثم حفظها كـ ProductImage
//        if (productDTO.getBase64Images() != null && !productDTO.getBase64Images().isEmpty()) {
//            List<ProductImage> images = productDTO.getBase64Images().stream()
//                    .map(base64 -> {
//                        String imageUrl = imageUploadService.uploadBase64Image(base64);
//                        ProductImage image = new ProductImage();
//                        image.setUrl(imageUrl);
//                        image.setProduct(product);
//                        return image;
//                    }).collect(Collectors.toList());
//
//            product.setImages(images);
//        }
//
//        return productMapper.toDTO(productRepository.save(product));
//    }

    public ProductDTO saveProductWithMultipart(ProductDTO productDTO, List<MultipartFile> images) {
        Product product = productMapper.toEntity(productDTO);

        if (images != null && !images.isEmpty()) {
            List<ProductImage> productImages = images.stream().map(file -> {
                String imageUrl = sharedImageUploadService.uploadMultipartFile(file); // هنضيف دي كمان
                ProductImage image = new ProductImage();
                image.setUrl(imageUrl);
                image.setProduct(product);
                return image;
            }).collect(Collectors.toList());

            product.setImages(productImages);
        }

        return productMapper.toDTO(productRepository.save(product));
    }


    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<ProductDTO> searchByName(String keyword) {
        return productRepository.findAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()))
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> filterByCategory(ProductCategory category) {
        return productRepository.findAll().stream()
                .filter(p -> p.getCategory().equals(category))
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> searchAndFilter(String keyword, ProductCategory category) {
        return productRepository.findAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()))
                .filter(p -> category == null || p.getCategory() == category)
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }
}
