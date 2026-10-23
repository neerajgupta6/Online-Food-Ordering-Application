package com.online.order.service;

import com.online.order.dto.*;
import com.online.order.entity.Category;
import com.online.order.entity.FoodItem;
import com.online.order.repository.CategoryRepository;
import com.online.order.repository.FoodItemRepository;
import com.online.order.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCatalogService {

    private final FoodItemRepository foodRepo;
    private final CategoryRepository categoryRepo;
    private final OrderItemRepository orderItemRepo;

    // ---------- Foods ----------

    @Transactional(readOnly = true)
    public List<FoodItemResponse> getAllFoods() {
        // includes unavailable foods too, so the admin can switch them back on
        return foodRepo.findAll(Sort.by("id")).stream()
                .map(FoodItemResponse::from).toList();
    }

    @Transactional
    public FoodItemResponse createFood(FoodRequest req) {
        FoodItem food = new FoodItem();
        applyRequest(food, req);
        return FoodItemResponse.from(foodRepo.save(food));
    }

    @Transactional
    public FoodItemResponse updateFood(Long id, FoodRequest req) {
        FoodItem food = foodRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Food item not found"));
        applyRequest(food, req);
        return FoodItemResponse.from(foodRepo.save(food));
    }

    @Transactional
    public void deleteFood(Long id) {
        if (!foodRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Food item not found");
        }
        if (orderItemRepo.existsByFoodId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "This food is in past orders, so it cannot be deleted. Mark it as unavailable instead.");
        }
        foodRepo.deleteById(id);
    }

    private void applyRequest(FoodItem food, FoodRequest req) {
        Category category = categoryRepo.findById(req.categoryId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Category not found"));

        food.setName(req.name().trim());
        food.setPrice(req.price());
        food.setCategory(category);
        food.setAvailable(req.available());
        food.setDescription(req.description() == null || req.description().isBlank()
                ? null : req.description().trim());
        food.setImage(req.image() == null || req.image().isBlank()
                ? null : req.image().trim());
    }

    // ---------- Categories ----------

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepo.findAll(Sort.by("name")).stream()
                .map(CategoryResponse::from).toList();
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest req) {
        String name = req.name().trim();
        if (categoryRepo.existsByNameIgnoreCase(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category already exists");
        }
        Category category = new Category();
        category.setName(name);
        return CategoryResponse.from(categoryRepo.save(category));
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        if (foodRepo.existsByCategoryId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "This category still has foods. Move or delete them first.");
        }
        categoryRepo.deleteById(id);
    }
}