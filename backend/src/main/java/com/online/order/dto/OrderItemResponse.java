package com.online.order.dto;

import com.online.order.entity.OrderItem;
import java.math.BigDecimal;

public record OrderItemResponse(
        Long foodId,
        String foodName,
        int quantity,
        BigDecimal price,
        BigDecimal subtotal
) {
    public static OrderItemResponse from(OrderItem i) {
        return new OrderItemResponse(
                i.getFood().getId(),
                i.getFood().getName(),
                i.getQuantity(),
                i.getPrice(),
                i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity()))
        );
    }
}