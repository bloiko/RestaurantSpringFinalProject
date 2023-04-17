package com.restaurant.service;

import com.restaurant.database.entity.FoodItem;
import com.restaurant.web.dto.GetMenuResponse;
import com.restaurant.web.dto.MenuPage;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class MenuService {
    private final FoodItemService foodItemService;

    private final CategoryService categoryService;

    public MenuService(FoodItemService foodItemService, CategoryService categoryService) {
        this.foodItemService = foodItemService;
        this.categoryService = categoryService;
    }

    public GetMenuResponse getMenuPage(MenuPage menuPage) {
        Page<FoodItem> pageFoodItems = foodItemService.getFoodItems(menuPage);

        return GetMenuResponse.builder()
                .page(menuPage.getPageNumber())
                .numOfPages(pageFoodItems.getTotalPages())
                .categories(categoryService.getAll())
                .foodItems(pageFoodItems.getContent())
                .build();
    }
}
