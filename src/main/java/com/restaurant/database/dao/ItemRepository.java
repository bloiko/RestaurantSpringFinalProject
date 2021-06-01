package com.restaurant.database.dao;


import com.restaurant.database.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Override
    Item getById(Long aLong);

    Item findByFoodItemAndQuantity(FoodItem foodItem,int quantity);
}















