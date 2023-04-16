package com.restaurant.web;

import com.restaurant.database.entity.Category;
import com.restaurant.service.CategoryService;
import com.restaurant.service.FoodItemService;
import com.restaurant.web.dto.CategoryDto;
import com.restaurant.web.dto.FoodItemRequest;
import com.restaurant.web.mapper.CategoryDtoMapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.hibernate.internal.util.StringHelper.isEmpty;

@RestController
@RequestMapping("/admin")
public class AdminPageController {
    private final CategoryService categoryService;

    private final FoodItemService foodItemService;

    private final CategoryDtoMapper categoryDtoMapper;

    public AdminPageController(CategoryService categoryService, FoodItemService foodItemService, CategoryDtoMapper categoryDtoMapper) {
        this.categoryService = categoryService;
        this.foodItemService = foodItemService;
        this.categoryDtoMapper = categoryDtoMapper;
    }

    @GetMapping("/category/{categoryId}")
    public CategoryDto getCategoryById(@PathVariable Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);

        return categoryDtoMapper.mapCategoryToDto(category);
    }

    @PostMapping("/category")
    public CategoryDto saveNewCategory(@Valid @RequestBody CategoryDto categoryDto) {
        if(isEmpty(categoryDto.getName())){
            throw new IllegalArgumentException("Category name should not be null or empty");
        }

        Category category = categoryService.saveNewCategory(categoryDto.getName());

        return categoryDtoMapper.mapCategoryToDto(category);
    }

    @PutMapping("/category/{categoryId}")
    public CategoryDto updateCategoryById(@PathVariable Long categoryId, @Valid @RequestBody CategoryDto categoryDto) {
        Category category = categoryService.updateCategoryName(categoryId, categoryDto.getName());

        return categoryDtoMapper.mapCategoryToDto(category);
    }

    @GetMapping ("/categories")
    public List<CategoryDto> getAllCategories() {
        List<Category> users = categoryService.getAllCategories();

        return users.stream().map(categoryDtoMapper::mapCategoryToDto).collect(Collectors.toList());
    }

    @DeleteMapping ("/category/{categoryId}")
    public String deleteCategoryById(@PathVariable Long categoryId) {
        return categoryService.deleteCategoryById(categoryId);
    }

    @PostMapping ("/food/item")
    public FoodItemRequest addNewFoodItem(@RequestBody FoodItemRequest foodItemRequest) {
        return foodItemService.addNewFoodItem(foodItemRequest);
    }

    @PutMapping ("/food/item")
    public FoodItemRequest updateFoodItem(@RequestBody FoodItemRequest foodItemRequest) {
        return foodItemService.updateFoodItem(foodItemRequest);
    }
}
