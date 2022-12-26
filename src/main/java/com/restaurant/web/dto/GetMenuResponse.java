package com.restaurant.web.dto;

import com.restaurant.database.entity.Category;
import com.restaurant.database.entity.FoodItem;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetMenuResponse {
    private int page;

    private int numOfPages;

    private List<Category> categories;

    private List<FoodItem> foodItems;
}
