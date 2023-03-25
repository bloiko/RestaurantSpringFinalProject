package com.restaurant.service;

import com.restaurant.database.dao.CategoryRepository;
import com.restaurant.database.entity.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hibernate.internal.util.StringHelper.isEmpty;

@Slf4j
@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.getById(categoryId);
    }

    public Category updateCategoryName(Long categoryId, String name) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (!categoryOptional.isPresent()) {
            throw new IllegalArgumentException("Category is not present by this id");
        }

        Category savedCategory = categoryOptional.get();
        savedCategory.setName(name);
        return categoryRepository.save(savedCategory);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public String deleteCategoryById(Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if(!optionalCategory.isPresent()){
            throw new IllegalArgumentException("Category is not present by this id");
        }

        // TODO check if we need to delete related foodItems and so on
        categoryRepository.delete(optionalCategory.get());
        return String.valueOf(categoryId);
    }

    public Category saveNewCategory(String name) {
        if(isEmpty(name)){
            throw new IllegalArgumentException("Category name should not be null or empty");
        }
        return categoryRepository.save(new Category(0L, name));
    }
}
