package com.PetCaretopia.order.service;


import com.PetCaretopia.order.DTO.OrderDTO;
import com.PetCaretopia.order.entity.*;
import com.PetCaretopia.order.mapper.OrderMapper;
import com.PetCaretopia.order.repository.*;
import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderMapper orderMapper;



    public List<OrderDTO> getUserOrders(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return orderRepository.findByUser(user).stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        return orderMapper.toDTO(order);
    }

    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderStatus currentStatus = order.getOrderStatus();

        // ⛔ ممنوع ترجع خطوة لورا
        if (newStatus.ordinal() < currentStatus.ordinal()) {
            throw new RuntimeException("Cannot revert order status from " + currentStatus + " to " + newStatus);
        }

        // ⛔ لو CANCELED أو DELIVERED خلاص قفلنا عليه
        if (currentStatus == OrderStatus.CANCELED || currentStatus == OrderStatus.DELIVERED) {
            throw new RuntimeException("Cannot update status of a completed or canceled order.");
        }

        // ✅ خصم الستوك عند الانتقال لـ PROCESSING فقط
        if (newStatus == OrderStatus.PROCESSING && currentStatus == OrderStatus.PENDING) {
            for (OrderItem item : order.getOrderItems()) {
                Product product = item.getProduct();
                product.decreaseStock(item.getQuantity());
                // Assuming productRepository موجود
                productRepository.save(product);
            }
        }

        order.setOrderStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        return orderMapper.toDTO(orderRepository.save(order));
    }



    public OrderDTO cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Cannot cancel processed order");
        }
        order.setOrderStatus(OrderStatus.CANCELED);
        return orderMapper.toDTO(orderRepository.save(order));
    }

    public BigDecimal getTotalSales() {
        return orderRepository.findAll().stream()
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public long getOrderCount() {
        return orderRepository.count();
    }
}
