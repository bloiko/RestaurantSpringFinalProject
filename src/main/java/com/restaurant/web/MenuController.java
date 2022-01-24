package com.restaurant.web;

import com.restaurant.database.entity.FoodItem;
import com.restaurant.service.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://restaurant-spring-project.herokuapp.com/")
public class MenuController {
    @Autowired
    private FoodItemService foodItemService;

    @GetMapping(value = {"/roma"})
    public List<FoodItem> getAllItems() {
        return foodItemService.getFoodItems();
    }
}
