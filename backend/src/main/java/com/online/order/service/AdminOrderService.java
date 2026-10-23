package com.online.order.service;

import com.online.order.dto.AdminOrderResponse;
import com.online.order.entity.Order;
import com.online.order.enums.OrderStatus;
import com.online.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final OrderRepository orderRepo;

    @Transactional(readOnly = true)
    public List<AdminOrderResponse> getAllOrders() {
        return orderRepo.findAllByOrderByCreatedAtDesc().stream()
                .map(AdminOrderResponse::from).toList();
    }

    @Transactional
    public AdminOrderResponse updateStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Order not found"));

        // only one step forward is allowed: PLACED -> PREPARING -> OUT_FOR_DELIVERY -> DELIVERED
        if (newStatus.ordinal() != order.getStatus().ordinal() + 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cannot change status from " + order.getStatus() + " to " + newStatus);
        }

        order.setStatus(newStatus);
        return AdminOrderResponse.from(orderRepo.save(order));
    }
}