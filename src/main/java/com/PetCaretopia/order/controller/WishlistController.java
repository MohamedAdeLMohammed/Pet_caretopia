package com.PetCaretopia.order.controller;

import com.PetCaretopia.order.DTO.ProductDTO;
import com.PetCaretopia.order.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    //  Add to wishlist
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    @PostMapping("/add")
    public void addToWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        wishlistService.addToWishlist(userId, productId);
    }

    //  Remove from wishlist
   @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    @DeleteMapping("/remove")
    public void removeFromWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        wishlistService.removeFromWishlist(userId, productId);
    }

    //  Toggle (add/remove)
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    @PostMapping("/toggle")
    public void toggleWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        wishlistService.toggleWishlist(userId, productId);
    }

    // Get user's wishlist
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    @GetMapping("/{userId}")
    public List<ProductDTO> getWishlist(@PathVariable Long userId) {
        return wishlistService.getWishlist(userId);
    }

    // Get wishlist count
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    @GetMapping("/count/{userId}")
    public int getWishlistCount(@PathVariable Long userId) {
        return wishlistService.getWishlistCount(userId);
    }

    //  Clear wishlist
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    @DeleteMapping("/clear/{userId}")
    public void clearWishlist(@PathVariable Long userId) {
        wishlistService.clearWishlist(userId);
    }

    //  Check if a product is in wishlist
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    @GetMapping("/check")
    public boolean isInWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        return wishlistService.isInWishlist(userId, productId);
    }
}
