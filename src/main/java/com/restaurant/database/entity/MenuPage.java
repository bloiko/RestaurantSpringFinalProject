package com.restaurant.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuPage {
    private int pageNumber = 0;
    private int pageSize = 5;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    //TODO change to use ENUM
    private String sortBy = "name";
    //TODO change to use ENUM
    private String filterBy;
}
