package com.restaurant.service;

import com.restaurant.RestaurantApplication;
import com.restaurant.database.entity.FoodItem;
import com.restaurant.database.entity.MenuPage;
import com.restaurant.web.dto.GetMenuResponse;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.restaurant.web.dto.MenuFilterBy.DESSERTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(classes = RestaurantApplication.class)
@TestPropertySource(locations = "classpath:application.properties")
public class MenuServiceIT {

    @Autowired
    private MenuService menuService;

    @Test
    public void testGetMenuPage() {
        final int pageSize = 10;
        Sort.Direction sortDirection = Sort.Direction.DESC;
        MenuPage menuPage = new MenuPage(1, pageSize, sortDirection, "name", "Desserts");

        GetMenuResponse menuResponse = menuService.getMenuPage(menuPage);

        assertEquals(1, menuResponse.getPage());
        List<FoodItem> actualFoodItems = menuResponse.getFoodItems();
        List<String> categoryNames = getCategoryNames(actualFoodItems);
        assertEquals(1, categoryNames.size());
        assertTrue(categoryNames.contains(DESSERTS.getValue()));

        List<FoodItem> sortedFoodItems = sortItemsByComparator(actualFoodItems, FoodItem::getName, sortDirection);
        assertEquals(sortedFoodItems, actualFoodItems);
        assertTrue(menuResponse.getFoodItems().size() <= pageSize);
    }

    @Test
    public void testGetMenuPageSortByCategoryDefault() {
        final int pageSize = 10;
        Sort.Direction sortDirection = Sort.Direction.ASC;
        MenuPage menuPage = new MenuPage(1, 3, sortDirection, null, "Desserts");

        GetMenuResponse menuResponse = menuService.getMenuPage(menuPage);

        assertEquals(1, menuResponse.getPage());
        List<FoodItem> actualFoodItems = menuResponse.getFoodItems();
        List<String> categoryNames = getCategoryNames(actualFoodItems);
        assertEquals(1, categoryNames.size());

        List<FoodItem> sortedFoodItems = sortItemsByComparator(actualFoodItems, foodItem -> foodItem.getCategory().getName(), sortDirection);
        assertEquals(sortedFoodItems, actualFoodItems);
        assertTrue(menuResponse.getFoodItems().size() <= pageSize);
    }

    private List<FoodItem> sortItemsByComparator(List<FoodItem> actualFoodItems,
                                                 Function<? super FoodItem, String> functionCompareBy,
                                                 Sort.Direction sortOrder) {
        Comparator<? super FoodItem> comparing = Comparator.comparing(functionCompareBy);
        if (sortOrder.equals(Sort.Direction.DESC)) {
            comparing = comparing.reversed();
        }
        return actualFoodItems.stream()
                .sorted(comparing)
                .collect(Collectors.toList());
    }

    @NotNull
    private static List<String> getCategoryNames(List<FoodItem> actualFoodItems) {
        return actualFoodItems.stream()
                .map(foodItem -> foodItem.getCategory().getName())
                .distinct()
                .collect(Collectors.toList());
    }
}