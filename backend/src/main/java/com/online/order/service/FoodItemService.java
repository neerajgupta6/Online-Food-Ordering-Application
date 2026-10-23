package com.online.order.service;

import com.online.order.dto.FoodItemResponse;
import com.online.order.entity.FoodItem;
import com.online.order.repository.FoodItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FoodItemService {

    private final FoodItemRepository foodRepo;

    public List<FoodItemResponse> getAll(Long categoryId, String search) {
        List<FoodItem> items;
        if (categoryId != null) {
            items = foodRepo.findByCategoryIdAndAvailableTrue(categoryId);
        } else if (search != null && !search.isBlank()) {
            items = foodRepo.findByNameContainingIgnoreCaseAndAvailableTrue(search);
        } else {
            items = foodRepo.findByAvailableTrue();
        }
        return items.stream().map(FoodItemResponse::from).toList();
    }

    public FoodItemResponse getById(Long id) {
        FoodItem item = foodRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Food item not found"));
        return FoodItemResponse.from(item);
    }
}