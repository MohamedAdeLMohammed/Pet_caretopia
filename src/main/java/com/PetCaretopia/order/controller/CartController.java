package com.PetCaretopia.order.controller;


import com.PetCaretopia.Security.Service.CustomUserDetails;
import jakarta.validation.constraints.Min;

import com.PetCaretopia.order.DTO.CartItemDTO;
import com.PetCaretopia.order.service.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public CartItemDTO addToCart(@RequestParam Long productId,
                                 @RequestParam @Min(1) int quantity,
                                 @AuthenticationPrincipal CustomUserDetails principal) {
        return cartService.addToCart(principal.getUserId(), productId, quantity);
    }


    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER')")
    @PutMapping("/update")
    public CartItemDTO updateCartItem(@RequestParam Long productId,
                                      @RequestParam @Min(1) int quantity,
                                      @AuthenticationPrincipal CustomUserDetails principal) {
        return cartService.updateCartItem(principal.getUserId(), productId, quantity);
    }


     @PreAuthorize("hasAnyRole('USER', 'PET_OWNER')")
     @DeleteMapping("/remove")
     public void removeCartItem(@RequestParam Long productId,
                                @AuthenticationPrincipal CustomUserDetails principal) {
         cartService.removeCartItem(principal.getUserId(), productId);
     }


    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER')")
    @DeleteMapping("/clear")
    public void clearCart(@AuthenticationPrincipal CustomUserDetails principal) {
        cartService.clearCart(principal.getUserId());
    }

    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER')")
    @GetMapping("/total")
    public BigDecimal getCartTotal(@AuthenticationPrincipal CustomUserDetails principal) {
        return cartService.getCartTotal(principal.getUserId());
    }

   @PreAuthorize("hasAnyRole('USER', 'PET_OWNER')")
   @GetMapping("/count")
   public int getCartItemCount(@AuthenticationPrincipal CustomUserDetails principal) {
       return cartService.getCartItemCount(principal.getUserId());
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
