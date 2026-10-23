package com.online.order.dto;

import com.online.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(
        @NotNull(message = "Status is required") OrderStatus status
) {}