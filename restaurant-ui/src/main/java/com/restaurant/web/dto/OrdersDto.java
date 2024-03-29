package com.restaurant.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDto {

    private Long id;

    private Timestamp orderDate;

    private int orderPrice;

    private List<FoodItemResponse> foodItems;

    private String orderStatus;

    private int promoCodeDiscount = 0;

}
