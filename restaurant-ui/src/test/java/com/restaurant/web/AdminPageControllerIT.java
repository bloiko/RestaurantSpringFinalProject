package com.restaurant.web;

import com.restaurant.RestaurantApplication;
import com.restaurant.database.dao.CategoryRepository;
import com.restaurant.database.dao.FoodRepository;
import com.restaurant.database.entity.Category;
import com.restaurant.database.entity.FoodItem;
import com.restaurant.web.dto.CategoryDto;
import com.restaurant.web.dto.FoodItemRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(classes = RestaurantApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql({"classpath:db-test/test-food-orders.sql"})
class AdminPageControllerIT {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private AdminPageController adminPageController;

    @Test
    void getCategoryById() {
        CategoryDto categoryDto = adminPageController.getCategoryById(1L);

        assertNotNull(categoryDto);
        assertEquals(1L, categoryDto.getId());
        assertEquals("Snacks", categoryDto.getName());
    }

    @Test
    void saveNewCategory() {
        String categoryName = "categoryName";
        CategoryDto categoryDto = new CategoryDto(0L, categoryName);
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);
        assertFalse(categoryOptional.isPresent());

        CategoryDto categoryResponse = adminPageController.saveNewCategory(categoryDto);

        assertNotNull(categoryResponse);
        categoryOptional = categoryRepository.findByName(categoryName);
        assertTrue(categoryOptional.isPresent());
        assertEquals(categoryOptional.get().getId(), categoryResponse.getId());
        assertEquals(categoryName, categoryResponse.getName());
    }

    @Test
    void updateCategoryById() {
        String categoryName = "someCategoryName";
        String anotherNameToUpdate = "anotherName";
        categoryRepository.save(new Category(0L, categoryName));
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);
        assertTrue(categoryOptional.isPresent());
        CategoryDto categoryDto = new CategoryDto(0L, anotherNameToUpdate);

        CategoryDto categoryResponse = adminPageController.updateCategoryById(categoryOptional.get().getId(), categoryDto);

        assertNotNull(categoryResponse);
        categoryOptional = categoryRepository.findByName(categoryName);
        assertFalse(categoryOptional.isPresent());

        categoryOptional = categoryRepository.findByName(anotherNameToUpdate);
        assertTrue(categoryOptional.isPresent());
        assertEquals(categoryOptional.get().getId(), categoryResponse.getId());
        assertEquals(anotherNameToUpdate, categoryResponse.getName());
    }

    @Test
    void getAllCategories() {
        List<CategoryDto> categoryDtos = adminPageController.getAllCategories();

        assertTrue(categoryDtos.size() > 3);
        categoryDtos.forEach(category -> {
            assertTrue(category.getId() > 0);
            assertNotNull(category.getName());
        });
    }

    @Test
    void deleteCategoryById() {
        String categoryName = "categoryNameToDelete";
        categoryRepository.save(new Category(0L, categoryName));
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);
        assertTrue(categoryOptional.isPresent());

        String categoryId = adminPageController.deleteCategoryById(categoryOptional.get().getId());

        assertEquals(String.valueOf(categoryOptional.get().getId()), categoryId);
        categoryOptional = categoryRepository.findByName(categoryName);
        assertFalse(categoryOptional.isPresent());
    }

    @Test
    void addNewFoodItem() {
        FoodItemRequest foodItemRequest = new FoodItemRequest(0L, "foodName", new BigDecimal(10), "image_url", 1L);

        FoodItemRequest foodItemResponse = adminPageController.addNewFoodItem(foodItemRequest);

        Optional<FoodItem> foodItemOptional = foodRepository.findById(foodItemResponse.getId());
        assertTrue(foodItemOptional.isPresent());
        FoodItem foodItem = foodItemOptional.get();
        assertEquals(foodItemResponse.getId(), foodItem.getId());
        assertEquals("foodName", foodItem.getName());
        assertEquals(new BigDecimal(10), foodItem.getPrice());
        assertEquals("image_url", foodItem.getImage());
        assertEquals("Snacks", foodItem.getCategory().getName());
    }

    @Test
    void updateFoodItem() {
        Category category = categoryRepository.getById(1L);
        FoodItem foodItem = foodRepository.save(new FoodItem(0L, "foodName1", new BigDecimal(10), "image_url_1",
            category));
        FoodItemRequest foodItemRequest = new FoodItemRequest(foodItem.getId(), "foodName2", new BigDecimal(12),
            "image_url_2", 2L);

        FoodItemRequest foodItemResponse = adminPageController.updateFoodItem(foodItemRequest);

        Optional<FoodItem> foodItemOptional = foodRepository.findById(foodItemResponse.getId());
        assertTrue(foodItemOptional.isPresent());
        foodItem = foodItemOptional.get();
        assertEquals(foodItemResponse.getId(), foodItem.getId());
        assertEquals("foodName2", foodItem.getName());
        assertEquals(new BigDecimal(12), foodItem.getPrice());
        assertEquals("image_url_2", foodItem.getImage());
        assertEquals("Desserts", foodItem.getCategory().getName());
    }
}