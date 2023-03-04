package com.restaurant.web;

import com.restaurant.database.entity.Item;
import com.restaurant.database.entity.Order;
import com.restaurant.service.OrderService;
import com.restaurant.service.UserService;
import com.restaurant.web.dto.FoodItemResponse;
import com.restaurant.web.dto.MyOrdersDto;
import com.restaurant.web.dto.MyOrdersResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class MyOrdersController {
    private final OrderService orderService;

    private final UserService userService;

    public MyOrdersController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/myorders1")
    public MyOrdersResponse getMyOrders() {
        String username = getCurrentUsername();

        List<Order> orders = getOrdersByUsername(username);
        return mapToMyOrdersResponse(orders);
    }

    private MyOrdersResponse mapToMyOrdersResponse(List<Order> orders) {
        List<MyOrdersDto> orderDtos = orders.stream().map(order -> new MyOrdersDto(order.getId(), order.getOrderDate(),
                order.getOrderPrice().intValue(), mapToFoodItemResponse(order.getItems()),
                order.getOrderStatus().getStatusName()))
                .collect(Collectors.toList());
        return new MyOrdersResponse(orderDtos);
    }

    private List<FoodItemResponse> mapToFoodItemResponse(List<Item> items) {
        return items.stream().map(item -> new FoodItemResponse(item.getFoodItem().getName(), item.getFoodItem().getPrice(), item.getQuantity())).collect(Collectors.toList());
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
