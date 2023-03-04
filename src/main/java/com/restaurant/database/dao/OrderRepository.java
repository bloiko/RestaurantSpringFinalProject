package com.restaurant.database.dao;


import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.OrderStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderStatus(OrderStatus status);

    List<Order> findAllByOrderStatusNot(OrderStatus status);

    List<Order> findAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}















