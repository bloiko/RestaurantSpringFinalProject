package com.restaurant.database.dao;


import com.restaurant.database.entity.Category;
import com.restaurant.database.entity.FoodItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<FoodItem, Long> {

    List<FoodItem> findAllByCategoryName(String name);

    Page<FoodItem> findAllByCategoryName(String name, Pageable pageable);
}















