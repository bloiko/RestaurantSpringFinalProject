package com.restaurant.database.dao;


import com.restaurant.database.entity.FoodItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FoodRepository extends JpaRepository<FoodItem, Long> {

    @Override
    List<FoodItem> findAll();

    @Override
    FoodItem getById(Long aLong);

    @Override
    Page<FoodItem> findAll(Pageable pageable);

    Page<FoodItem> findAllByCategory(String category, Pageable pageable);
}















