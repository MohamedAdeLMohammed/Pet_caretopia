package com.PetCaretopia.order.controller;


import jakarta.validation.constraints.Min;

import com.PetCaretopia.order.DTO.CartItemDTO;
import com.PetCaretopia.order.service.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER')")
    @GetMapping("/{userId}")
    public List<CartItemDTO> getCartItems(@PathVariable Long userId) {
        return cartService.getCartItems(userId);
    }

    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER')")
    @PostMapping("/add")
    public CartItemDTO addToCart(@RequestParam Long userId,
                                 @RequestParam Long productId,
                                 @RequestParam @Min(1) int quantity) {
        return cartService.addToCart(userId, productId, quantity);
    }

    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER')")
    @PutMapping("/update")
    public CartItemDTO updateCartItem(@RequestParam Long userId,
                                      @RequestParam Long productId,
                                      @RequestParam @Min(1) int quantity) {
        return cartService.updateCartItem(userId, productId, quantity);
    }

     @PreAuthorize("hasAnyRole('USER', 'PET_OWNER')")
    @DeleteMapping("/remove")
    public void removeCartItem(@RequestParam Long userId,
                               @RequestParam Long productId) {
        cartService.removeCartItem(userId, productId);
    }

    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER')")
    @DeleteMapping("/clear/{userId}")
    public void clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
    }

    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER')")
    @GetMapping("/total/{userId}")
    public BigDecimal getCartTotal(@PathVariable Long userId) {
        return cartService.getCartTotal(userId);
    }

   @PreAuthorize("hasAnyRole('USER', 'PET_OWNER')")
    @GetMapping("/count/{userId}")
    public int getCartItemCount(@PathVariable Long userId) {
        return cartService.getCartItemCount(userId);
    }

    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER')")
    @GetMapping("/items")
    public List<CartItemDTO> getAllCartItems() {
        return cartService.getAllCartItems();
    }

    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER')")
    @DeleteMapping("/item/{itemId}")
    public void deleteCartItemById(@PathVariable Long itemId) {
        cartService.deleteCartItemById(itemId);
    }
}
