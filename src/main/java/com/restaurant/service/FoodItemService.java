package com.restaurant.service;


import com.restaurant.database.dao.CategoryRepository;
import com.restaurant.database.dao.FoodRepository;
import com.restaurant.database.entity.Category;
import com.restaurant.database.entity.FoodItem;
import com.restaurant.database.entity.Item;
import com.restaurant.database.entity.MenuPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Food Item service.
 *
 * @author B.Loiko
 */
@Service
public class FoodItemService {
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public void addFoodItemToCart(List<Item> cart, String foodId) {
        CartService cartService = new CartService();
        int index = cartService.isExisting(Integer.parseInt(foodId), cart);
        if (cart.isEmpty() || index == -1) {
            FoodItem foodItem = foodRepository.findById(Long.valueOf(foodId)).get();
            Item item = Item.builder().id(0L).foodItem(foodItem).quantity(1).build();
            cart.add(item);
        } else {
            int quantity = cart.get(index).getQuantity() + 1;
            cart.get(index).setQuantity(quantity);
        }
    }

    @Transactional
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public List<FoodItem> getFoodItems() {
        return foodRepository.findAll();
    }

    @Transactional
    public List<FoodItem> getFoodItems(MenuPage menuPage) {
        Sort sortBy;
        if (menuPage.getSortBy() != null) {
            sortBy = Sort.by(menuPage.getSortDirection(), menuPage.getSortBy());
        } else {
            sortBy = Sort.by(menuPage.getSortDirection(), "category");
        }
        Pageable pageable = PageRequest.of(menuPage.getPageNumber(), menuPage.getPageSize(), sortBy);
        String filterBy = menuPage.getFilterBy();
        if (menuPage.getFilterBy() == null) {
            return foodRepository.findAll(pageable).getContent();
        } else {
            return foodRepository.findAllByCategory(categoryRepository.findByName(filterBy),
                    pageable).getContent();
        }
    }
}
