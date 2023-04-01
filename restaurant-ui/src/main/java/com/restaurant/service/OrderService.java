package com.restaurant.service;


import com.restaurant.database.entity.ActionType;
import com.restaurant.database.entity.EntityType;
import com.restaurant.database.dao.FoodRepository;
import com.restaurant.database.dao.OrderRepository;
import com.restaurant.database.dao.OrderStatusRepository;
import com.restaurant.database.dao.PromoCodeRepository;
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
import java.util.Optional;
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

    private final PromoCodeRepository promoCodeRepository;

    private final AuditSender auditSender;

    public OrderService(OrderRepository orderRepository, OrderStatusRepository statusRepository, UserService userService,
                        FoodRepository foodRepository, PromoCodeRepository promoCodeRepository, AuditSender auditSender) {
        this.orderRepository = orderRepository;
        this.statusRepository = statusRepository;
        this.userService = userService;
        this.foodRepository = foodRepository;
        this.promoCodeRepository = promoCodeRepository;
        this.auditSender = auditSender;
    }

    public Long addOrderAndGetId(List<Item> items, User user, String promoCode) {
        if (isEmpty(items)) {
            throw new IllegalArgumentException("List of items cannot be null or empty");
        }

        Optional<PromoCode> promoCodeOptional = promoCodeRepository.findByCode(promoCode);
        int discount = 0;
        PromoCode promoCodeObject = null;
        if(promoCodeOptional.isPresent() && promoCodeOptional.get().isActive()){
            promoCodeObject = promoCodeOptional.get();
            discount = promoCodeObject.getDiscount();
        }

        OrderStatus waitingStatus = statusRepository.findByStatusName(WAITING_STATUS);

        Order order = Order.builder()
                .id(0L)
                .user(user)
                .orderDate(new Timestamp(new Date().getTime()))
                .orderStatus(waitingStatus)
                .items(items)
                .orderPrice(getPriceSumOfAllItems(items, discount))
                .promoCode(promoCodeObject)
                .build();

        for (Item item : items){
            item.setOrder(order);
        }
        return orderRepository.save(order).getId();
    }

    @NotNull
    private static BigDecimal getPriceSumOfAllItems(List<Item> items, int discount) {
        BigDecimal price = new BigDecimal(0);
        for (Item item : items) {
            price = price.add(new BigDecimal(item.getQuantity() * item.getFoodItem().getPrice()));
        }
        BigDecimal priceDiscount = price.multiply(new BigDecimal(discount).divide(new BigDecimal(100)));
        return price.subtract(priceDiscount);
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

    public Long orderFoodItems(List<FoodItemDto> foodItemsDto, String promoCode) {
        User user = getCurrentUser();
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        List<Item> itemsToOrder = createItems(foodItemsDto);

        Long orderId = addOrderAndGetId(itemsToOrder, user, promoCode);

        auditSender.addAudit(orderId, EntityType.ORDER, ActionType.CREATE_ORDER);
        return orderId;
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