package com.restaurant.web;

import com.restaurant.database.entity.FoodItem;
import com.restaurant.service.FoodItemService;
import com.restaurant.web.dto.MenuFilterBy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu1")
public class MenuController {
    private final FoodItemService foodItemService;

    public MenuController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    @GetMapping
    public List<FoodItem> getAllItemsByFilter(@RequestParam MenuFilterBy filter) {
        return foodItemService.getFoodItemsFilterBy(filter);
    }
}
