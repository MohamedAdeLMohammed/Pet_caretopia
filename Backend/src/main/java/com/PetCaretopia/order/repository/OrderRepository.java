package com.PetCaretopia.order.repository;


import com.PetCaretopia.order.entity.Order;
import com.PetCaretopia.order.entity.OrderStatus;
import com.PetCaretopia.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    List<Order> findByOrderStatus(OrderStatus status);
}
