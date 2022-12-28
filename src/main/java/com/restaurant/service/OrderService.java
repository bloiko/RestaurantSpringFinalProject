package com.restaurant.service;


import com.restaurant.database.dao.FoodRepository;
import com.restaurant.database.dao.OrderRepository;
import com.restaurant.database.dao.OrderStatusRepository;
import com.restaurant.database.entity.*;
import com.restaurant.web.dto.FoodItemDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Order service.
 *
 * @author B.Loiko
 */
@Service
@Transactional
public class OrderService {
    public static final String WAITING_STATUS = "WAITING";

    public static final String DONE_STATUS = "DONE";

    private final OrderRepository orderRepository;

    private final OrderStatusRepository statusRepository;

    private final UserService userService;

    private final FoodRepository foodRepository;

    public OrderService(OrderRepository orderRepository, OrderStatusRepository statusRepository, UserService userService, FoodRepository foodRepository) {
        this.orderRepository = orderRepository;
        this.statusRepository = statusRepository;
        this.userService = userService;
        this.foodRepository = foodRepository;
    }

    public Long addOrderAndGetId(List<Item> items, User user) {
        if (isEmpty(items)) {
            throw new IllegalArgumentException("List of items cannot be null or empty");
        }
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        OrderStatus waitingStatus = statusRepository.findByStatusName(WAITING_STATUS);

        Order order = Order.builder()
                .id(0L)
                .user(user)
                .orderDate(new Timestamp(new Date().getTime()))
                .orderStatus(waitingStatus)
                .items(items)
                .orderPrice(getPriceSumOfAllItems(items))
                .build();

        return orderRepository.save(order).getId();
    }

    @NotNull
    private static BigDecimal getPriceSumOfAllItems(List<Item> items) {
        BigDecimal price = new BigDecimal(0);
        for (Item item : items) {
            price = price.add(new BigDecimal(item.getQuantity() * item.getFoodItem().getPrice()));
        }
        return price;
    }

    public List<OrderStatus> getStatuses() {
        return statusRepository.findAll();
    }

    public List<Order> getDoneOrders() {
        return orderRepository.findAllByOrderStatus(statusRepository.findByStatusName(DONE_STATUS));
    }

    public List<Order> getNotDoneOrdersSortByIdDesc() {
        return orderRepository.findAllByOrderStatusNot(statusRepository.findByStatusName(DONE_STATUS))
                .stream()
                .sorted(Comparator.comparing(Order::getId).reversed())
                .collect(Collectors.toList());
    }

    public Order getOrder(String orderIdString) {
        return orderRepository.getById(Long.valueOf(orderIdString));
    }

    public void updateOrder(Long id, OrderStatus newStatus) {
        Order order = orderRepository.getById(id);
        order.setOrderStatus(newStatus);
        orderRepository.save(order);
    }

    public Long orderFoodItems(List<FoodItemDto> foodItemsDto) {
        User user = getCurrentUser();

        List<Item> itemsToOrder = createItems(foodItemsDto);
        return addOrderAndGetId(itemsToOrder, user);
    }

    @NotNull
    private List<Item> createItems(List<FoodItemDto> foodItemsDto) {
        List<Long> foodItemIds = foodItemsDto.stream()
                .map(FoodItemDto::getId)
                .collect(Collectors.toList());

        List<FoodItem> foodItemsFromDb = foodRepository.findAllById(foodItemIds);

        return foodItemsFromDb.stream()
                .map(foodItem -> buildItem(foodItemsDto, foodItem))
                .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        return userService.getUserByUserName(username);
    }

    @NotNull
    private static Item buildItem(List<FoodItemDto> foodItemsDto, FoodItem foodItem) {
        return new Item(0L, foodItem, getQuantityByFoodItemId(foodItemsDto, foodItem.getId()), null);
    }

    private static int getQuantityByFoodItemId(List<FoodItemDto> foodItemsDto, Long foodItemId) {
        return foodItemsDto.stream().filter(foodItemDto -> foodItemDto.getId().equals(foodItemId)).findAny().get().getQuantity();
    }
}