package com.online.order.repository;

import com.online.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    boolean existsByFoodId(Long foodId);
}