package com.restaurant.service;


import com.restaurant.database.dao.OrderRepository;
import com.restaurant.database.dao.OrderStatusRepository;
import com.restaurant.database.entity.Item;
import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.OrderStatus;
import com.restaurant.database.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Order service.
 *
 * @author B.Loiko
 */
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderStatusRepository statusRepository;

    public Long addOrderAndGetId(List<Item> cart, User user) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Timestamp(new Date().getTime()));
        order.setOrderStatus(OrderStatus.WAITING);
        if (cart != null && !cart.isEmpty()) {
            order.setItems(cart);
            order = orderRepository.save(order);
        }
        return order.getId();
    }

    public List<OrderStatus> getStatuses() {
        return statusRepository.findAll();
    }

    public List<Order> getDoneOrders() {
        return orderRepository.findAllByOrderStatus(OrderStatus.DONE);
    }

    public List<Order> getNotDoneOrdersSortById(){
        return orderRepository.findAllByOrderStatusNot(OrderStatus.DONE);
    }

    public Order getOrder(String orderIdString) {
        return orderRepository.getById(Long.valueOf(orderIdString));
    }

    public void updateOrder(Long id, OrderStatus newStatus) {
        Order order = orderRepository.getById( id);
        order.setOrderStatus(newStatus);
        orderRepository.save(order);
    }
}