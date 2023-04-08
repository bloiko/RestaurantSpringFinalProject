package com.restaurant.database.dao;


import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o " +
            "join o.orderStatus os " +
            "where os.statusName in :orderStatuses " +
            "order by o.orderDate ASC ")
    List<Order> findAllByOrderStatusNamesAndOrderByOrderDateAsc(List<Status> orderStatuses);

    List<Order> findAllByUserId(Long userId);

    List<Order> findAllByOrderDateBetween(Timestamp startDate, Timestamp endDate);

    void deleteAllByUserId(Long userId);
}















