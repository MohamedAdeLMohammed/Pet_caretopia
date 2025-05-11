package com.PetCaretopia.order.service;


import com.PetCaretopia.order.DTO.OrderDTO;
import com.PetCaretopia.order.entity.*;
import com.PetCaretopia.order.mapper.OrderMapper;
import com.PetCaretopia.order.repository.CartItemRepository;
import com.PetCaretopia.order.repository.CartRepository;
import com.PetCaretopia.order.repository.OrderItemRepository;
import com.PetCaretopia.order.repository.OrderRepository;
import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
public class CheckoutService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private OrderMapper orderMapper;

    @Transactional
    public OrderDTO processCheckout(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Cart cart = cartRepository.findByUser(user).orElseThrow();
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            product.decreaseStock(cartItem.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());

            orderItems.add(orderItem);
            total = total.add(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(total);

        cartItemRepository.deleteByCart(cart);
        return orderMapper.toDTO(orderRepository.save(order));
    }
}


