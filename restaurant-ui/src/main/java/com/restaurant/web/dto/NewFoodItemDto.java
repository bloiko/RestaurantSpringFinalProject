package com.restaurant.web.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewFoodItemDto {
    private String image;

    private String name;

    private BigDecimal price;

    private String categoryName;
}
