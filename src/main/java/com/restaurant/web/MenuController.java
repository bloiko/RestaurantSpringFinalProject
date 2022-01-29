package com.restaurant.web;

import com.restaurant.database.entity.FoodItem;
import com.restaurant.service.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MenuController {
    @Autowired
    private FoodItemService foodItemService;

    @GetMapping(value = {"/menu1"})
    public List<FoodItem> getAllItemsByFilter(@RequestParam String filter) {
        return foodItemService.getFoodItemsFilterBy(filter);
    }

}
