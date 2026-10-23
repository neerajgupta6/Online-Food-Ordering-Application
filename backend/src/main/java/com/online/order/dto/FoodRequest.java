package com.online.order.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record FoodRequest(
        @NotBlank(message = "Food name is required") String name,
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be more than 0") BigDecimal price,
        @NotNull(message = "Category is required") Long categoryId,
        String image,
        @Size(max = 500, message = "Description is too long (max 500)") String description,
        boolean available
) {}