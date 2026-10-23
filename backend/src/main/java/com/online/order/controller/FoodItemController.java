package com.online.order.controller;

import com.online.order.dto.FoodItemResponse;
import com.online.order.service.FoodItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
public class FoodItemController {

    private final FoodItemService foodService;

    @GetMapping
    public List<FoodItemResponse> getAll(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search) {
        return foodService.getAll(categoryId, search);
    }

    @GetMapping("/{id}")
    public FoodItemResponse getById(@PathVariable Long id) {
        return foodService.getById(id);
    }
}