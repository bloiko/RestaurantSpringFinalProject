package com.restaurant.web;

import com.restaurant.database.entity.Category;
import com.restaurant.database.entity.User;
import com.restaurant.service.CategoryService;
import com.restaurant.web.dto.CategoryDto;
import com.restaurant.web.dto.PasswordDto;
import com.restaurant.web.dto.UserDto;
import com.restaurant.web.mapper.CategoryDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    private final CategoryDtoMapper categoryDtoMapper;

    public CategoryController(CategoryService categoryService, CategoryDtoMapper categoryDtoMapper) {
        this.categoryService = categoryService;
        this.categoryDtoMapper = categoryDtoMapper;
    }


}
