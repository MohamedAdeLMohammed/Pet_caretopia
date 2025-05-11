package com.PetCaretopia.order.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public OrderDTO placeOrder(@RequestParam Long userId) {
        return checkoutService.processCheckout(userId);
    }
}
