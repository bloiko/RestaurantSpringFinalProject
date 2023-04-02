package com.restaurant.web;

import com.restaurant.database.entity.Order;
import com.restaurant.service.UserService;
import com.restaurant.web.dto.OrdersResponse;
import com.restaurant.web.mapper.OrderDtoMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping
public class MyOrdersController {

    private final UserService userService;

    private final OrderDtoMapper orderDtoMapper;

    public MyOrdersController(UserService userService, OrderDtoMapper orderDtoMapper) {
        this.userService = userService;
        this.orderDtoMapper = orderDtoMapper;
    }

    @GetMapping("/myorders1")
    public OrdersResponse getMyOrders() {
        String username = getCurrentUsername();

        List<Order> orders = getOrdersByUsername(username);
        return orderDtoMapper.mapToMyOrdersResponse(orders);
    }

    private List<Order> getOrdersByUsername(String username) {
        List<Order> orders = new LinkedList<>();
        if (username != null) {
            orders = userService.getUserOrdersSortByOrderDateReversed(username);
        }
        return orders;
    }

    private String getCurrentUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}
