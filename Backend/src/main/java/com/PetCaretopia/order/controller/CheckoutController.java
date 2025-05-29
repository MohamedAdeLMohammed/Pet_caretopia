package com.PetCaretopia.order.controller;


import com.PetCaretopia.Security.Service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.PetCaretopia.order.DTO.OrderDTO;
import com.PetCaretopia.order.service.CheckoutService;



@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER')")
    @PostMapping("/place")
    public OrderDTO placeOrder(@AuthenticationPrincipal CustomUserDetails principal) {
        return checkoutService.processCheckout(principal.getUserId());
    }

}

