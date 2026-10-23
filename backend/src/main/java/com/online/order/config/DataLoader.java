package com.online.order.config;

import com.online.order.entity.Category;
import com.online.order.entity.FoodItem;
import com.online.order.repository.CategoryRepository;
import com.online.order.repository.FoodItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepo;
    private final FoodItemRepository foodRepo;

    @Override
    public void run(String... args) {
        if (categoryRepo.count() > 0) return;

        Category pizza = new Category();
        pizza.setName("Pizza");
        categoryRepo.save(pizza);

        Category burger = new Category();
        burger.setName("Burger");
        categoryRepo.save(burger);

        foodRepo.save(food("Margherita Pizza", "249", "Classic cheese pizza", pizza));
        foodRepo.save(food("Paneer Tikka Pizza", "329", "Spicy paneer topping", pizza));
        foodRepo.save(food("Veg Burger", "129", "Crispy veg patty burger", burger));
        foodRepo.save(food("Cheese Burger", "159", "Double cheese burger", burger));
    }

    private FoodItem food(String name, String price, String desc, Category cat) {
        FoodItem f = new FoodItem();
        f.setName(name);
        f.setPrice(new BigDecimal(price));
        f.setDescription(desc);
        f.setCategory(cat);
        f.setAvailable(true);
        return f;
    }
}