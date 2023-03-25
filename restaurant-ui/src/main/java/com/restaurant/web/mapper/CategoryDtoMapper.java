package com.restaurant.web.mapper;

import com.restaurant.database.entity.Category;
import com.restaurant.web.dto.CategoryDto;
import org.springframework.stereotype.Component;

@Component
public class CategoryDtoMapper {

    public CategoryDtoMapper() {
    }

    public CategoryDto mapCategoryToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}