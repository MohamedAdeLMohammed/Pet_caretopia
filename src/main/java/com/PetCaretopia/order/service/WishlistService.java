package com.PetCaretopia.order.service;

import com.PetCaretopia.order.DTO.ProductDTO;
import com.PetCaretopia.order.entity.Product;

import com.PetCaretopia.order.entity.WishlistItem;
import com.PetCaretopia.order.mapper.ProductMapper;
import com.PetCaretopia.order.repository.ProductRepository;
import com.PetCaretopia.order.repository.WishlistItemRepository;
import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    @Autowired
    private WishlistItemRepository wishlistItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    public void addToWishlist(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        boolean exists = wishlistItemRepository.existsByUserAndProduct(user, product);
        if (!exists) {
            WishlistItem item = new WishlistItem();
            item.setUser(user);
            item.setProduct(product);
            wishlistItemRepository.save(item);
        }
    }

    @Transactional
    public void removeFromWishlist(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        wishlistItemRepository.deleteByUserAndProduct(user, product);
    }

    public List<ProductDTO> getWishlist(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return wishlistItemRepository.findByUser(user).stream()
                .map(item -> productMapper.toDTO(item.getProduct()))
                .collect(Collectors.toList());
    }

    public boolean isInWishlist(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return wishlistItemRepository.existsByUserAndProduct(user, product);
    }

    public void toggleWishlist(Long userId, Long productId) {
        if (isInWishlist(userId, productId)) {
            removeFromWishlist(userId, productId);
        } else {
            addToWishlist(userId, productId);
        }
    }

    public void clearWishlist(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        wishlistItemRepository.deleteByUser(user);
    }

    public int getWishlistCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return wishlistItemRepository.countByUser(user);
    }
}
