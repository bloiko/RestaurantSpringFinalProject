package com.restaurant.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodItemRequest {

    private Long id;

    @NotEmpty(message = "Name should not be empty!")
    private String name;

    @Min(value = 0, message = "Price should be positive number!")
    private int price;

    @NotEmpty(message = "Image URL should not be empty!")
    private String image;

    private Long categoryId;
}
