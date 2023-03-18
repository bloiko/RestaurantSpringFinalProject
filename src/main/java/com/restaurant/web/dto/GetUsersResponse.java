package com.restaurant.web.dto;

import com.restaurant.database.entity.Category;
import com.restaurant.database.entity.FoodItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUsersResponse {
    private int page;

    private int numOfPages;

    private List<UserDto> users;
}
