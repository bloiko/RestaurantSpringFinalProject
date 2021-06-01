package com.restaurant.service;


import com.restaurant.database.dao.OrderStatusRepository;
import com.restaurant.database.entity.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Order service.
 *
 * @author B.Loiko
 */
@Service
public class OrderStatusService {
    @Autowired
    private OrderStatusRepository orderStatusRepository;
    @Transactional
    public List<OrderStatus> findAll(){
        return orderStatusRepository.findAll();
    }
    @Transactional
    public OrderStatus findByStatusName(String name){
        return orderStatusRepository.findByStatusName(name);
    }

    @Transactional
    public <S extends OrderStatus> S save(S s){
        return orderStatusRepository.save(s);
    }
}