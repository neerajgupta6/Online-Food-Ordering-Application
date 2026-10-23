package com.online.order.repository;

import com.online.order.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItem,Long> {
    List<FoodItem> findByAvailableTrue();
    List<FoodItem> findByCategoryIdAndAvailableTrue(Long categoryId);
    List<FoodItem> findByNameContainingIgnoreCaseAndAvailableTrue(String name);
    boolean existsByCategoryId(Long categoryId);
}
