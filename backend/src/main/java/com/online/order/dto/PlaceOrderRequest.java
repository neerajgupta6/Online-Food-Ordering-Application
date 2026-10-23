package com.online.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record PlaceOrderRequest(
        @NotBlank(message = "Delivery address is required")
        @Size(min = 10, max = 300, message = "Address must be 10 to 300 characters")
        String address,

        @NotEmpty(message = "Cart is empty")
        @Valid
        List<ItemRequest> items
) {
    public record ItemRequest(
            @NotNull(message = "Food id is required") Long foodId,
            @Min(value = 1, message = "Quantity must be at least 1")
            @Max(value = 50, message = "Quantity is too large") int quantity
    ) {}
}