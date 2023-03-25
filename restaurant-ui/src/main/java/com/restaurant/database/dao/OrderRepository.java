package com.restaurant.database.dao;


import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.OrderStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderStatus(OrderStatus status);

    List<Order> findAllByOrderStatusNot(OrderStatus status);

    List<Order> findAllByUserId(Long userId);

    List<Order> findAllByOrderDateBetween(Timestamp startDate, Timestamp endDate);

    void deleteAllByUserId(Long userId);
}















