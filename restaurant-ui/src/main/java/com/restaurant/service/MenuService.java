package com.restaurant.service;

import com.restaurant.database.entity.FoodItem;
import com.restaurant.web.dto.MenuPage;
import com.restaurant.web.dto.GetMenuResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class MenuService {
    private final FoodItemService foodItemService;

    public MenuService(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    public GetMenuResponse getMenuPage(MenuPage menuPage) {
        Page<FoodItem> pageFoodItems = foodItemService.getFoodItems(menuPage);

        return GetMenuResponse.builder()
                .page(menuPage.getPageNumber())
                .numOfPages(pageFoodItems.getTotalPages())
                .categories(foodItemService.getCategories())
                .foodItems(pageFoodItems.getContent())
                .build();
    }
}
