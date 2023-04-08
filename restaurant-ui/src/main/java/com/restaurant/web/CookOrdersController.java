package com.restaurant.web;

import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.Status;
import com.restaurant.service.OrderService;
import com.restaurant.web.dto.OrdersResponse;
import com.restaurant.web.mapper.OrderDtoMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/cook")
public class CookOrdersController {

    private final OrderService orderService;

    private final OrderDtoMapper orderDtoMapper;

    public CookOrdersController(OrderService orderService, OrderDtoMapper orderDtoMapper) {
        this.orderService = orderService;
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
}
