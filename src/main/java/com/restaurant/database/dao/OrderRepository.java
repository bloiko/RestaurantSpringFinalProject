package com.restaurant.database.dao;



import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.OrderStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Override
    List<Order> findAll();
    List<Order> findAllByOrderStatus(OrderStatus status);
     List<Order> findAllByOrderStatusNot(OrderStatus status);

     List<Order> findAllByUserId(Long userId);

    @Override
    Optional<Order> findById(Long aLong);

    @Override
    void deleteById(Long aLong);
}















