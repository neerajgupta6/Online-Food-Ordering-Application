package com.online.order.controller;

import com.online.order.dto.AdminOrderResponse;
import com.online.order.dto.UpdateStatusRequest;
import com.online.order.service.AdminOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService service;

    @GetMapping
    public List<AdminOrderResponse> getAll() {
        return service.getAllOrders();
    }

    @PutMapping("/{id}/status")
    public AdminOrderResponse updateStatus(@PathVariable Long id,
                                           @Valid @RequestBody UpdateStatusRequest req) {
        return service.updateStatus(id, req.status());
    }
}