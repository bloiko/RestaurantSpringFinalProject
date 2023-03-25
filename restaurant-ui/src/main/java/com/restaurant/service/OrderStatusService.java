package com.restaurant.service;


import com.restaurant.database.dao.OrderStatusRepository;
import com.restaurant.database.entity.OrderStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Order service.
 *
 * @author B.Loiko
 */
@Service
@Transactional
public class OrderStatusService {
    private final OrderStatusRepository orderStatusRepository;

    public OrderStatusService(OrderStatusRepository orderStatusRepository) {
        this.orderStatusRepository = orderStatusRepository;
    }

    public OrderStatus findByStatusName(String name){
        return orderStatusRepository.findByStatusName(name);
    }
}