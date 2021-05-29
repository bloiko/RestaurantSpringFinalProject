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

import java.util.List;

/**
 * Food Item service.
 *
 * @author B.Loiko
 */
public class FoodItemService {
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public void addFoodItemToCart(List<Item> cart, String foodId) {
        int index = isExisting(foodId, cart);
        if (cart.isEmpty() || index == -1) {
            Item item = new Item();
            item.setFoodItem(foodRepository.getById(Long.valueOf(foodId)));
            item.setQuantity(1);
            cart.add(item);
        } else {
            int quantity = cart.get(index).getQuantity() + 1;
            cart.get(index).setQuantity(quantity);
        }
    }

    public int isExisting(String id, List<Item> cart) {
        int foodId = Integer.parseInt(id);
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getFoodItem().getId() == foodId) {
                return i;
            }
        }
        return -1;
    }

    public List<Category> getCategories(){
        return categoryRepository.findAll();
    }

    public List<FoodItem> getFoodItems() {
        return foodRepository.findAll();
    }

    /*   //TO Do
       public List<FoodItem> getFoodItems(int page, int number, String sortBy, String order, String filter) throws DBException {
           int offset = (page - 1) * number;
           int itemsLimit = number;

           if (sortBy == null && filter != null) {
               return foodRepository.getFoodItemsWithSkipLimitFilter(itemsLimit, offset, filter);
           } else if (sortBy == null && filter == null) {
               return foodRepository.getFoodItemsWithSkipAndLimit(itemsLimit, offset);
           } else if (filter == null && sortBy != null) {
               return foodRepository.getFoodItemsWithSkipLimitAndOrder(itemsLimit, offset, sortBy, order);
           } else {
               return foodRepository.getFoodItemsWithFilterSkipLimitAndOrder(filter, itemsLimit, offset, sortBy, order);
           }
       }*/
    //TO Do
    public List<FoodItem> getFoodItems(MenuPage menuPage) {
        Sort sortBy = Sort.by(menuPage.getSortDirection(), menuPage.getSortBy());
        Pageable pageable = PageRequest.of(menuPage.getPageNumber(), menuPage.getPageSize(), sortBy);
        String filterBy = menuPage.getFilterBy();
        if (menuPage.getFilterBy() != null) {
            return foodRepository.findAll(pageable).getContent();
        } else {
            return foodRepository.findAllByCategory(filterBy, pageable).getContent();
        }
    }
}
