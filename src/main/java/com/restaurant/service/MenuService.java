package com.restaurant.service;

import com.restaurant.database.entity.FoodItem;
import com.restaurant.database.entity.MenuPage;
import com.restaurant.web.dto.GetMenuResponse;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static org.thymeleaf.util.StringUtils.isEmpty;

@Service
@Transactional
public class MenuService {
    private final FoodItemService foodItemService;

    public MenuService(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    public GetMenuResponse getMenuPage(MenuPage menuPage) {
        List<FoodItem> filteredFoodItems = foodItemService.getFoodItems(menuPage);

        int numOfPages = getNumOfPages(filteredFoodItems, menuPage.getPageSize());
        return GetMenuResponse.builder()
                .page(menuPage.getPageNumber())
                .numOfPages(numOfPages)
                .categories(foodItemService.getCategories())
                .foodItems(filteredFoodItems)
                .build();
    }

    private int getNumOfPages(List<FoodItem> foodItems, int itemsPerPage) {
        int modOfTheDivision = foodItems.size() % itemsPerPage;
        int incorrectNumOfPages = foodItems.size() / itemsPerPage;
        return modOfTheDivision == 0 ? incorrectNumOfPages : incorrectNumOfPages + 1;
    }
}
