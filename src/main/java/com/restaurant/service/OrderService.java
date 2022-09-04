package com.restaurant.service;


import com.restaurant.database.dao.ItemRepository;
import com.restaurant.database.dao.OrderRepository;
import com.restaurant.database.dao.OrderStatusRepository;
import com.restaurant.database.entity.Item;
import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.OrderStatus;
import com.restaurant.database.entity.User;
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
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderStatusRepository statusRepository;
    private final ItemRepository itemRepository;

    public OrderService(OrderRepository orderRepository, OrderStatusRepository statusRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.statusRepository = statusRepository;
        this.itemRepository = itemRepository;
    }

    public Long addOrderAndGetId(List<Item> cart, User user) {
        Timestamp orderDate = new Timestamp(new Date().getTime());
        OrderStatus orderStatus = statusRepository.findByStatusName("WAITING");
        Order order = Order.builder()
                .id(0L)
                .user(user)
                .orderDate(orderDate)
                .orderStatus(orderStatus).build();
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

    public List<OrderStatus> getStatuses() {
        return statusRepository.findAll();
    }

    public List<Order> getDoneOrders() {
        return orderRepository.findAllByOrderStatus(statusRepository.findByStatusName("DONE"));
    }

    public List<Order> getNotDoneOrdersSortByIdDesc(){
        return orderRepository.findAllByOrderStatusNot(statusRepository.findByStatusName("DONE"))
                .stream()
                .sorted(Comparator.comparing(Order::getId).reversed())
                .collect(Collectors.toList());
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