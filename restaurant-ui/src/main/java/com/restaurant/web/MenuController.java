package com.restaurant.web;

import com.restaurant.database.entity.FoodItem;
import com.restaurant.web.dto.MenuPage;
import com.restaurant.service.FoodItemService;
import com.restaurant.service.MenuService;
import com.restaurant.web.dto.GetMenuResponse;
import com.restaurant.web.dto.MenuFilterBy;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping
public class MenuController {
    private final FoodItemService foodItemService;

    private final MenuService menuService;

    public MenuController(FoodItemService foodItemService, MenuService menuService) {
        this.foodItemService = foodItemService;
        this.menuService = menuService;
    }

    @GetMapping("/menu1")
    @Deprecated //Categories cannot be enum
    public List<FoodItem> getAllItemsByFilter(@RequestParam MenuFilterBy filter) {
        return foodItemService.getFoodItemsFilterBy(filter);
    }

    @PostMapping(value = {"/menu1"})
    public GetMenuResponse getMenuPage(@RequestBody MenuPage menuPageRequest) {
        return menuService.getMenuPage(menuPageRequest);
    }
}