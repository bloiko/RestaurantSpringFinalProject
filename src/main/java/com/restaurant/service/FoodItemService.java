package com.restaurant.service;


import com.restaurant.database.dao.CategoryRepository;
import com.restaurant.database.dao.FoodRepository;
import com.restaurant.database.entity.Category;
import com.restaurant.database.entity.FoodItem;
import com.restaurant.database.entity.Item;
import com.restaurant.database.entity.MenuPage;
import com.restaurant.web.dto.FoodItemRequest;
import com.restaurant.web.dto.MenuFilterBy;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.restaurant.web.dto.MenuFilterBy.ALL_CATEGORIES;


/**
 * Food Item service.
 *
 * @author B.Loiko
 */
@Service
@Transactional
public class FoodItemService {
    public static final String DEFAULT_SORT_BY_FILTER = "category";

    private final FoodRepository foodRepository;

    private final CategoryRepository categoryRepository;

    private final CartService cartService;

    public FoodItemService(FoodRepository foodRepository, CategoryRepository categoryRepository, CartService cartService) {
        this.foodRepository = foodRepository;
        this.categoryRepository = categoryRepository;
        this.cartService = cartService;
    }

    public FoodItemRequest addNewFoodItem(FoodItemRequest request) {
        Optional<Category> optionalCategory = categoryRepository.findById(request.getCategoryId());
        if (!optionalCategory.isPresent()) {
            throw new IllegalArgumentException("Category not exists with this id " + request.getCategoryId());
        }

        FoodItem foodItem = new FoodItem(0L, request.getName(), request.getPrice(), request.getImage(), optionalCategory.get());
        foodRepository.save(foodItem);

        request.setId(foodItem.getId());
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
        return request;
    }

    @Deprecated
    public void addFoodItemToCart(List<Item> cart, String foodId) {
        int index = cartService.isExisting(Integer.parseInt(foodId), cart);
        if (cart.isEmpty() || index == -1) {
            FoodItem foodItem = foodRepository.findById(Long.valueOf(foodId)).get();
            Item item = Item.builder()
                    .id(0L)
                    .foodItem(foodItem)
                    .quantity(1)
                    .build();
            cart.add(item);
        } else {
            int quantity = cart.get(index).getQuantity() + 1;
            cart.get(index).setQuantity(quantity);
        }
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public List<FoodItem> getFoodItems() {
        return foodRepository.findAll();
    }

    public List<FoodItem> getFoodItemsFilterBy(MenuFilterBy filter) {
        if (ALL_CATEGORIES.equals(filter)) {
            return getFoodItems();
        }

        return foodRepository.findAllByCategoryName(filter.getValue());
    }

    public List<FoodItem> getFoodItems(MenuPage menuPage) {
        Sort sort = getSortFromDto(menuPage);

        Pageable pageable = PageRequest.of(menuPage.getPageNumber() - 1, menuPage.getPageSize(), sort);

        return getFoodItems(menuPage.getFilterBy(), pageable);
    }

    private List<FoodItem> getFoodItems(String filterBy, Pageable pageable) {
        boolean filterAllCategories = filterBy == null;
        Page<FoodItem> foodItemsPage;
        if (filterAllCategories) {
            foodItemsPage = foodRepository.findAll(pageable);
        } else {
            foodItemsPage = foodRepository.findAllByCategoryName(filterBy, pageable);
        }
        return foodItemsPage.getContent();
    }

    @NotNull
    private static Sort getSortFromDto(MenuPage menuPage) {
        String sortBy = menuPage.getSortBy() != null ? menuPage.getSortBy() : DEFAULT_SORT_BY_FILTER;

        return Sort.by(menuPage.getSortDirection(), sortBy);
    }
}
