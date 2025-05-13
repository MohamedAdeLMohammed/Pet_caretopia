package com.PetCaretopia.order.controller;

import com.PetCaretopia.Security.Service.CustomUserDetails;
import com.PetCaretopia.order.DTO.ProductDTO;
import com.PetCaretopia.order.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public void addToWishlist(@RequestParam Long productId,
                              @AuthenticationPrincipal CustomUserDetails principal) {
        wishlistService.addToWishlist(principal.getUserId(), productId);
    }


    //  Remove from wishlist
   @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
   @DeleteMapping("/remove")
   public void removeFromWishlist(@RequestParam Long productId,
                                  @AuthenticationPrincipal CustomUserDetails principal) {
       wishlistService.removeFromWishlist(principal.getUserId(), productId);
   }


    //  Toggle (add/remove)
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    @PostMapping("/toggle")
    public void toggleWishlist(@RequestParam Long productId,
                               @AuthenticationPrincipal CustomUserDetails principal) {
        wishlistService.toggleWishlist(principal.getUserId(), productId);
    }


    // Get user's wishlist
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    @GetMapping
    public List<ProductDTO> getWishlist(@AuthenticationPrincipal CustomUserDetails principal) {
        return wishlistService.getWishlist(principal.getUserId());
    }

    // Get wishlist count
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    @GetMapping("/count")
    public int getWishlistCount(@AuthenticationPrincipal CustomUserDetails principal) {
        return wishlistService.getWishlistCount(principal.getUserId());
    }

    //  Clear wishlist
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    @DeleteMapping("/clear")
    public void clearWishlist(@AuthenticationPrincipal CustomUserDetails principal) {
        wishlistService.clearWishlist(principal.getUserId());
    }

    //  Check if a product is in wishlist
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    @GetMapping("/check")
    public boolean isInWishlist(@RequestParam Long productId,
                                @AuthenticationPrincipal CustomUserDetails principal) {
        return wishlistService.isInWishlist(principal.getUserId(), productId);
    }

}
