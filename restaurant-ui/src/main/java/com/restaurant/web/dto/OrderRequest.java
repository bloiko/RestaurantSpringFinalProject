package com.restaurant.web.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private List<FoodItemDto> foodItems;

    private String promoCode;

    public OrderRequest(List<FoodItemDto> foodItems) {
        this.foodItems = foodItems;
    }
}
