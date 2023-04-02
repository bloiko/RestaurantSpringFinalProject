package com.restaurant.web;

import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.Status;
import com.restaurant.service.OrderService;
import com.restaurant.service.UserService;
import com.restaurant.web.dto.OrdersResponse;
import com.restaurant.web.mapper.OrderDtoMapper;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/cook")
public class CookOrdersController {

    private final OrderService orderService;

    private final UserService userService;

    private final OrderDtoMapper orderDtoMapper;

    public CookOrdersController(OrderService orderService, UserService userService, OrderDtoMapper orderDtoMapper) {
        this.orderService = orderService;
        this.userService = userService;
        this.orderDtoMapper = orderDtoMapper;
    }

    @GetMapping("/orders")
    public OrdersResponse getOrdersForCook() {
        List<Order> orders = orderService.getOrdersForCook();

        return orderDtoMapper.mapToMyOrdersResponse(orders);
    }

    @PutMapping("/order/{orderId}")
    public boolean changeOrderStatus(@PathVariable Long orderId, @RequestParam Status orderStatus) {
        return orderService.changeOrderStatusByCook(orderId, orderStatus);
    }

    private List<Order> getOrdersByUsername(String username) {
        List<Order> orders = new LinkedList<>();
        if (username != null) {
            orders = userService.getUserOrdersSortByOrderDateReversed(username);
        }
        return orders;
    }
}
