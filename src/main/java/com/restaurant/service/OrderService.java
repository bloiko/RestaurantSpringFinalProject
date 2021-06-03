package com.restaurant.service;


import com.restaurant.database.dao.ItemRepository;
import com.restaurant.database.dao.OrderRepository;
import com.restaurant.database.dao.OrderStatusRepository;
import com.restaurant.database.entity.Item;
import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.OrderStatus;
import com.restaurant.database.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Order service.
 *
 * @author B.Loiko
 */
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderStatusRepository statusRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Transactional
    public Long addOrderAndGetId(List<Item> cart, User user) {
        Order order = new Order();
        order.setId(0L);
        order.setUser(user);
        order.setOrderDate(new Timestamp(new Date().getTime()));
        order.setOrderStatus(statusRepository.findByStatusName("WAITING"));
        order = orderRepository.save(order);
        if (cart != null && !cart.isEmpty()) {
            BigDecimal price = new BigDecimal(0);
            for (int i = 0; i < cart.size(); i++) {
                Item item = cart.get(i);
                cart.get(i).setOrder(order);
                cart.set(i,itemRepository.save(item));
                price =price.add(new BigDecimal(item.getQuantity()*item.getFoodItem().getPrice()));
            }
            order.setOrderPrice(price);
            order.setItems(cart);
            order = orderRepository.save(order);
        }
        return order.getId();
    }
    @Transactional
    public List<OrderStatus> getStatuses() {
        return statusRepository.findAll();
    }
    @Transactional
    public List<Order> getDoneOrders() {
        return orderRepository.findAllByOrderStatus(statusRepository.findByStatusName("DONE"));
    }
    @Transactional
    public List<Order> getNotDoneOrdersSortByIdDesc(){
        return orderRepository.findAllByOrderStatusNot(statusRepository.findByStatusName("DONE"))
                .stream()
                .sorted(Comparator.comparing(Order::getId).reversed())
                .collect(Collectors.toList());
    }
    @Transactional
    public Order getOrder(String orderIdString) {
        return orderRepository.getById(Long.valueOf(orderIdString));
    }
    @Transactional
    public void updateOrder(Long id, OrderStatus newStatus) {
        Order order = orderRepository.getById( id);
        order.setOrderStatus(newStatus);
        orderRepository.save(order);
    }
}