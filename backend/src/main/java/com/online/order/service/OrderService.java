package com.online.order.service;

import com.online.order.dto.OrderResponse;
import com.online.order.dto.PlaceOrderRequest;
import com.online.order.entity.FoodItem;
import com.online.order.entity.Order;
import com.online.order.entity.OrderItem;
import com.online.order.entity.User;
import com.online.order.enums.OrderStatus;
import com.online.order.repository.FoodItemRepository;
import com.online.order.repository.OrderRepository;
import com.online.order.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final FoodItemRepository foodRepo;

    @Transactional
    public OrderResponse placeOrder(String email, PlaceOrderRequest req) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "User not found"));

        // if the same food is sent twice, add the quantities together
        Map<Long, Integer> quantities = new LinkedHashMap<>();
        for (PlaceOrderRequest.ItemRequest i : req.items()) {
            quantities.merge(i.foodId(), i.quantity(), Integer::sum);
        }

        Order order = new Order();
        order.setUser(user);
        order.setAddress(req.address().trim());
        order.setStatus(OrderStatus.PLACED);

        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : quantities.entrySet()) {
            FoodItem food = foodRepo.findById(entry.getKey())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Food item not found: " + entry.getKey()));

            if (!food.isAvailable()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, food.getName() + " is not available right now");
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setFood(food);
            item.setQuantity(entry.getValue());
            item.setPrice(food.getPrice());          // price from the database
            order.getItems().add(item);

            total = total.add(food.getPrice().multiply(BigDecimal.valueOf(entry.getValue())));
        }

        order.setTotal(total);
        orderRepo.save(order);                       // saves the order and its items together

        return OrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders(String email) {
        return orderRepo.findByUserEmailOrderByCreatedAtDesc(email)
                .stream()
                .map(OrderResponse::from)
                .toList();
    }
}