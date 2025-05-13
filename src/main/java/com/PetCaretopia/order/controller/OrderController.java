package com.PetCaretopia.order.controller;

import com.PetCaretopia.Security.Service.CustomUserDetails;
import com.PetCaretopia.order.DTO.OrderDTO;
import com.PetCaretopia.order.entity.OrderStatus;
import com.PetCaretopia.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    @GetMapping
    public List<OrderDTO> getUserOrders(@AuthenticationPrincipal CustomUserDetails principal) {
        return orderService.getUserOrders(principal.getUserId());
    }


    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    @GetMapping("/details/{orderId}")
    public OrderDTO getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }


    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    @PutMapping("/cancel/{orderId}")
    public OrderDTO cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/count")
    public long getOrderCount() {
        return orderService.getOrderCount();
    }

    @PreAuthorize("hasRole('ADMIN')")
    //  New: update order status with stock handling
    @PutMapping("/status")
    public OrderDTO updateOrderStatus(
            @RequestParam Long orderId,
            @RequestParam String status) {
        return orderService.updateOrderStatus(orderId, OrderStatus.valueOf(status.toUpperCase()));
    }
}
