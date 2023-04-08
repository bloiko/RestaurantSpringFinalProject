package com.restaurant.web.mapper;

import com.restaurant.database.entity.Item;
import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.PromoCode;
import com.restaurant.web.dto.FoodItemResponse;
import com.restaurant.web.dto.OrdersDto;
import com.restaurant.web.dto.OrdersResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDtoMapper {

    public OrderDtoMapper() {
    }

    public OrdersResponse mapToMyOrdersResponse(List<Order> orders) {
        List<OrdersDto> orderDtos = orders.stream().map(order -> {
                    PromoCode promoCode = order.getPromoCode();
                    int discount = 0;
                    if (promoCode != null) {
                        discount = promoCode.getDiscount();
                    }
                    return new OrdersDto(order.getId(), order.getOrderDate(),
                            order.getOrderPrice().intValue(), mapToFoodItemResponse(order.getItems()),
                            order.getOrderStatus().getStatusName().name(), discount);
                })
                .collect(Collectors.toList());
        return new OrdersResponse(orderDtos);
    }

    private List<FoodItemResponse> mapToFoodItemResponse(List<Item> items) {
        return items.stream().map(item -> new FoodItemResponse(item.getFoodItem().getName(), item.getFoodItem().getPrice(), item.getQuantity())).collect(Collectors.toList());
    }
}