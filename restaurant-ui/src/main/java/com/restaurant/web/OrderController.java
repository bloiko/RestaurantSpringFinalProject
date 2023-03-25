package com.restaurant.web;

import com.restaurant.service.OrderService;
import com.restaurant.web.dto.OrderRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/cart/order1")
    public Long orderCart(@RequestBody OrderRequest orderRequest) {
        return orderService.orderFoodItems(orderRequest.getFoodItems());
    }
}
