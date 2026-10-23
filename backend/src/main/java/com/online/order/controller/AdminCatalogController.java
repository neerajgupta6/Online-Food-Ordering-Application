package com.online.order.controller;

import com.online.order.dto.*;
import com.online.order.service.AdminCatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminCatalogController {

    private final AdminCatalogService service;

    // ----- Foods -----
    @GetMapping("/foods")
    public List<FoodItemResponse> getFoods() {
        return service.getAllFoods();
    }

    @PostMapping("/foods")
    @ResponseStatus(HttpStatus.CREATED)
    public FoodItemResponse createFood(@Valid @RequestBody FoodRequest req) {
        return service.createFood(req);
    }

    @PutMapping("/foods/{id}")
    public FoodItemResponse updateFood(@PathVariable Long id,
                                       @Valid @RequestBody FoodRequest req) {
        return service.updateFood(id, req);
    }

    @DeleteMapping("/foods/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(@PathVariable Long id) {
        service.deleteFood(id);
    }

    // ----- Categories -----
    @GetMapping("/categories")
    public List<CategoryResponse> getCategories() {
        return service.getAllCategories();
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(@Valid @RequestBody CategoryRequest req) {
        return service.createCategory(req);
    }

    @DeleteMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        service.deleteCategory(id);
    }
}