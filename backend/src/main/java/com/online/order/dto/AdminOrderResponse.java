package com.online.order.dto;

import com.online.order.entity.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AdminOrderResponse(
        Long id,
        String customerName,
        String customerEmail,
        BigDecimal total,
        String status,
        String address,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) {
    public static AdminOrderResponse from(Order o) {
        return new AdminOrderResponse(
                o.getId(),
                o.getUser().getName(),
                o.getUser().getEmail(),
                o.getTotal(),
                o.getStatus().name(),
                o.getAddress(),
                o.getCreatedAt(),
                o.getItems().stream().map(OrderItemResponse::from).toList()
        );
    }
}