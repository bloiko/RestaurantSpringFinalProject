package com.restaurant.service;


import com.restaurant.database.dao.CategoryRepository;
import com.restaurant.database.dao.FoodRepository;
import com.restaurant.database.entity.ActionType;
import com.restaurant.database.entity.Category;
import com.restaurant.database.entity.EntityType;
import com.restaurant.database.entity.FoodItem;
import com.restaurant.web.dto.FoodItemRequest;
import com.restaurant.web.dto.MenuPage;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


/**
 * Food Item service.
 *
 * @author B.Loiko
 */
@Service
@Transactional
public class FoodItemService {
    public static final String DEFAULT_SORT_BY_FILTER = "category";

    public static final String ALL_CATEGORIES = "All categories";

    private final FoodRepository foodRepository;

    private final CategoryRepository categoryRepository;

    private final AuditSender auditSender;

    public FoodItemService(FoodRepository foodRepository, CategoryRepository categoryRepository,
                           AuditSender auditSender) {
        this.foodRepository = foodRepository;
        this.categoryRepository = categoryRepository;
        this.auditSender = auditSender;
    }

    public FoodItemRequest addNewFoodItem(FoodItemRequest request) {
        Optional<Category> optionalCategory = categoryRepository.findById(request.getCategoryId());
        if (!optionalCategory.isPresent()) {
            throw new IllegalArgumentException("Category not exists with this id " + request.getCategoryId());
        }

        FoodItem foodItem = new FoodItem(0L, request.getName(), request.getPrice(), request.getImage(), optionalCategory.get());
        foodItem = foodRepository.save(foodItem);

        request.setId(foodItem.getId());

        auditSender.addAudit(foodItem.getId(), EntityType.FOOD_ITEM, ActionType.CREATE_FOOD_ITEM);
        return request;
    }

    public FoodItemRequest updateFoodItem(FoodItemRequest request) {
        Optional<Category> optionalCategory = categoryRepository.findById(request.getCategoryId());
        if (!optionalCategory.isPresent()) {
            throw new IllegalArgumentException("Category not exists with this id " + request.getCategoryId());
        }
        Optional<FoodItem> optionalFoodItem = foodRepository.findById(request.getId());
        if (!optionalFoodItem.isPresent()) {
            throw new IllegalArgumentException("FoodItem not exists with this id " + request.getId());
        }

        FoodItem foodItem = optionalFoodItem.get();
        foodItem.setCategory(optionalCategory.get());
        foodItem.setName(request.getName());
        foodItem.setImage(request.getImage());
        foodItem.setPrice(request.getPrice());
        foodRepository.save(foodItem);
        request.setId(foodItem.getId());

        auditSender.addAudit(foodItem.getId(), EntityType.FOOD_ITEM, ActionType.UPDATE_FOOD_ITEM);
        return request;
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Page<FoodItem> getFoodItems(MenuPage menuPage) {
        Sort sort = getSortFromDto(menuPage);

        Pageable pageable = PageRequest.of(menuPage.getPageNumber() - 1, menuPage.getPageSize(), sort);

        return getFoodItems(menuPage.getFilterBy(), pageable);
    }

    private Page<FoodItem> getFoodItems(String filterBy, Pageable pageable) {
        if (filterBy == null || filterBy.equals(ALL_CATEGORIES)) {
            return foodRepository.findAll(pageable);
        } else {
            return foodRepository.findAllByCategoryName(filterBy, pageable);
        }
    }

    @NotNull
    private static Sort getSortFromDto(MenuPage menuPage) {
        String sortBy = menuPage.getSortBy() != null ? menuPage.getSortBy() : DEFAULT_SORT_BY_FILTER;

        return Sort.by(menuPage.getSortDirection(), sortBy);
    }
}
