package com.restaurant.database.dao;


import com.restaurant.database.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
    @Override
    List<OrderStatus> findAll();

    @Override
    <S extends OrderStatus> S save(S s);
}















