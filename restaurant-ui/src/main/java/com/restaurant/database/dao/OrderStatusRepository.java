package com.restaurant.database.dao;

import com.restaurant.database.entity.OrderStatus;
import com.restaurant.database.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {

    OrderStatus findByStatusName(Status statusName);
}















