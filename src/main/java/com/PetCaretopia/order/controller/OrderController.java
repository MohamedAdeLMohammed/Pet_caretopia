package com.PetCaretopia.order.controller;

import com.PetCaretopia.order.DTO.OrderDTO;
import com.PetCaretopia.order.entity.OrderStatus;
import com.PetCaretopia.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{userId}")
    public List<OrderDTO> getUserOrders(@PathVariable Long userId) {
        return orderService.getUserOrders(userId);
    }

    @GetMapping("/details/{orderId}")
    public OrderDTO getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @PutMapping("/cancel/{orderId}")
    public OrderDTO cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }

    @GetMapping("/all")
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/count")
    public long getOrderCount() {
        return orderService.getOrderCount();
    }

    //  New: update order status with stock handling
    @PutMapping("/status")
    public OrderDTO updateOrderStatus(
            @RequestParam Long orderId,
            @RequestParam String status) {
        return orderService.updateOrderStatus(orderId, OrderStatus.valueOf(status.toUpperCase()));
    }
}
