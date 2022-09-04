package com.restaurant.web;

import com.restaurant.database.entity.FoodItem;
import com.restaurant.service.FoodItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MenuController {
    private final FoodItemService foodItemService;

    public MenuController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    @GetMapping(value = {"/menu"})
    public List<FoodItem> getAllItemsByFilter(@RequestParam String filter) {
        return foodItemService.getFoodItemsFilterBy(filter);
    }

}
