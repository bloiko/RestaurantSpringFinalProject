package com.restaurant.service;

import com.restaurant.database.dao.CategoryRepository;
import com.restaurant.database.entity.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class CategoryService extends ReaderServiceImpl<Category> {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
    }

    public Category updateCategoryName(Long categoryId, String name) {
        Category category = getById(categoryId);
        category.setName(name);

        return categoryRepository.save(category);
    }

    public Category saveNewCategory(String name) {
        Optional<Category> categoryOptional = categoryRepository.findByName(name);
        if (categoryOptional.isPresent()) {
            throw new IllegalArgumentException("Category is present with this name");
        }

        return categoryRepository.save(new Category(0L, name));
    }

    public String deleteById(Long id) {
        Category category = getById(id);

        categoryRepository.delete(category);
        return String.valueOf(id);
    }
}
