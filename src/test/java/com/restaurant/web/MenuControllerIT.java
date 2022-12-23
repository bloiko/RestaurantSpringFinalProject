package com.restaurant.web;

import com.restaurant.RestaurantApplication;
import com.restaurant.database.dao.FoodRepository;
import com.restaurant.database.entity.FoodItem;
import com.restaurant.web.dto.MenuFilterBy;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.restaurant.web.dto.MenuFilterBy.*;
import static org.assertj.core.util.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(classes = RestaurantApplication.class)
@TestPropertySource(locations="classpath:application.properties")
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