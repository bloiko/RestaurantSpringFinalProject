package com.restaurant.web;

import com.restaurant.database.entity.Item;
import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.PromoCode;
import com.restaurant.service.OrderService;
import com.restaurant.service.UserService;
import com.restaurant.web.dto.FoodItemResponse;
import com.restaurant.web.dto.OrdersDto;
import com.restaurant.web.dto.OrdersResponse;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/cook")
public class CookOrdersController {

    private final OrderService orderService;

    private final UserService userService;

    public CookOrdersController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/orders")
    public OrdersResponse getOrdersForCook() {
        List<Order> orders = orderService.getOrdersForCook();

        return mapToMyOrdersResponse(orders);
    }

    @PutMapping("/order/{orderId}")
    public boolean changeOrderStatus(@PathVariable Long orderId, @RequestParam String orderStatus) {
        return orderService.changeOrderStatusByCook(orderId, orderStatus);
    }

    private OrdersResponse mapToMyOrdersResponse(List<Order> orders) {
        List<OrdersDto> orderDtos = orders.stream().map(order -> {
                    PromoCode promoCode = order.getPromoCode();
                    int discount = 0;
                    if (promoCode != null) {
                        discount = promoCode.getDiscount();
                    }
                    return new OrdersDto(order.getId(), order.getOrderDate(),
                            order.getOrderPrice().intValue(), mapToFoodItemResponse(order.getItems()),
                            order.getOrderStatus().getStatusName(), discount);
                })
                .collect(Collectors.toList());
        return new OrdersResponse(orderDtos);
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
}
