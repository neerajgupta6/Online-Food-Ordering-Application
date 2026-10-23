package com.online.order.dto;

import com.online.order.entity.FoodItem;
import java.math.BigDecimal;

public record FoodItemResponse(
        Long id,
        String name,
        BigDecimal price,
        String image,
        String description,
        boolean available,
        Long categoryId,
        String categoryName
) {
    public static FoodItemResponse from(FoodItem f) {
        return new FoodItemResponse(
                f.getId(),
                f.getName(),
                f.getPrice(),
                f.getImage(),
                f.getDescription(),
                f.isAvailable(),
                f.getCategory() != null ? f.getCategory().getId() : null,
                f.getCategory() != null ? f.getCategory().getName() : null
        );
    }
}