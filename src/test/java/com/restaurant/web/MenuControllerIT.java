package com.restaurant.web;

import com.restaurant.RestaurantApplication;
import com.restaurant.database.dao.FoodRepository;
import com.restaurant.database.entity.FoodItem;
import com.restaurant.web.dto.MenuPage;
import com.restaurant.web.dto.GetMenuResponse;
import com.restaurant.web.dto.MenuFilterBy;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.restaurant.web.dto.MenuFilterBy.*;
import static org.assertj.core.util.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(classes = RestaurantApplication.class)
@TestPropertySource(locations="classpath:application-test.properties")
@Sql({"classpath:db-test/test-food-data.sql"})
class MenuControllerIT {

    @Autowired
    private MenuController menuController;

    @Autowired
    private FoodRepository foodRepository;

    @Test
    void getAllItemsByFilterAllCategories() {
        List<FoodItem> foodItems = menuController.getAllItemsByFilter(ALL_CATEGORIES);

        assertCategoriesInResponse(foodItems, newArrayList(SNACKS.getValue(), DESSERTS.getValue()));
        assertEquals(foodRepository.findAll().size(), foodItems.size());
    }

    @ParameterizedTest
    @MethodSource("provideMenuFilters")
    void getAllItemsByFilter(MenuFilterBy filterBy) {
        List<FoodItem> foodItems = menuController.getAllItemsByFilter(filterBy);

        assertCategoriesInResponse(foodItems, newArrayList(filterBy.getValue()));
        assertEquals(foodRepository.findAllByCategoryName(filterBy.getValue()).size(), foodItems.size());
    }

    private static Stream<Arguments> provideMenuFilters() {
        return Stream.of(
                Arguments.of(SNACKS),
                Arguments.of(DESSERTS)
        );
    }

    @Test
    public void testGetMenuPage() {
        final int pageSize = 10;
        Sort.Direction sortDirection = Sort.Direction.DESC;
        MenuPage menuPage = new MenuPage(1, pageSize, sortDirection, "name", "Desserts");

        GetMenuResponse menuResponse = menuController.getMenuPage(menuPage);

        assertEquals(1, menuResponse.getPage());
        List<FoodItem> actualFoodItems = menuResponse.getFoodItems();
        List<String> categoryNames = getCategoryNames(actualFoodItems);
        assertEquals(1, categoryNames.size());
        assertTrue(categoryNames.contains(DESSERTS.getValue()));

        List<FoodItem> sortedFoodItems = sortItemsByComparator(actualFoodItems, FoodItem::getName, sortDirection);
        assertEquals(sortedFoodItems, actualFoodItems);
        assertTrue(menuResponse.getFoodItems().size() <= pageSize);
        assertTrue(menuResponse.getFoodItems().size() > 0);

    }

    @Test
    public void testGetMenuPageSortByCategoryDefault() {
        final int pageSize = 10;
        Sort.Direction sortDirection = Sort.Direction.ASC;
        MenuPage menuPage = new MenuPage(1, 3, sortDirection, null, "Desserts");

        GetMenuResponse menuResponse = menuController.getMenuPage(menuPage);

        assertEquals(1, menuResponse.getPage());
        List<FoodItem> actualFoodItems = menuResponse.getFoodItems();
        List<String> categoryNames = getCategoryNames(actualFoodItems);
        assertEquals(1, categoryNames.size());

        List<FoodItem> sortedFoodItems = sortItemsByComparator(actualFoodItems, foodItem -> foodItem.getCategory().getName(), sortDirection);
        assertEquals(sortedFoodItems, actualFoodItems);
        assertTrue(menuResponse.getFoodItems().size() <= pageSize);
        assertTrue(menuResponse.getFoodItems().size() > 0);
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

    private static void assertCategoriesInResponse(List<FoodItem> actualFoodItems, List<String> expectedCategories) {
        List<String> categoryNames = getDistinctCategoryList(actualFoodItems);
        assertEquals(expectedCategories.size(), categoryNames.size());
        expectedCategories.forEach(categoryName -> assertTrue(categoryNames.contains(categoryName)));
    }

    @NotNull
    private static List<String> getDistinctCategoryList(List<FoodItem> foodItems) {
        return foodItems.stream()
                .map(foodItem -> foodItem.getCategory().getName())
                .distinct()
                .collect(Collectors.toList());
    }
}