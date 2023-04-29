package com.restaurant.service;

import com.restaurant.database.dao.OrderRepository;
import com.restaurant.database.dao.UserRepository;
import com.restaurant.database.entity.Item;
import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.PromoCode;
import com.restaurant.database.entity.User;
import com.restaurant.web.dto.ExcelBuilderDto;
import com.restaurant.web.dto.ResourceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ExcelService {

    private final ExcelBuilderService<User> excelUserBuilderService;

    private final ExcelBuilderService<Order> excelOrderBuilderService;

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    public ExcelService(ExcelBuilderService<User> excelUserBuilderService, ExcelBuilderService<Order> excelOrderBuilderService,
                        OrderRepository orderRepository, UserRepository userRepository) {
        this.excelUserBuilderService = excelUserBuilderService;
        this.excelOrderBuilderService = excelOrderBuilderService;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }


    public ResourceDTO exportMonthOrders(Timestamp startDate, Timestamp endDate) {
        List<Order> orderList = orderRepository.findAllByOrderDateBetween(startDate, endDate);
        String[] columnNames = {"Order ID", "Order Date", "User Full Name", "User Email",
                "Order Status", "Food Item + Quantity + Price of one Item", "Full price", "Discount"};
        List<Function<Order, String>> functions = new ArrayList<>();
        functions.add(orderFunc -> String.valueOf(orderFunc.getId()));
        functions.add(orderFunc -> String.valueOf(orderFunc.getOrderDate()));
        functions.add(orderFunc -> orderFunc.getUser().getFirstName() + " " + orderFunc.getUser().getLastName());
        functions.add(orderFunc -> orderFunc.getUser().getEmail());
        functions.add(orderFunc -> orderFunc.getOrderStatus().getStatusName().name());
        functions.add(orderFunc -> {
            List<Item> items = orderFunc.getItems();
            StringBuilder stringBuilder = new StringBuilder();
            items.forEach(item -> stringBuilder.append(item.getFoodItem().getName())
                    .append("    ")
                    .append(item.getQuantity())
                    .append(" items * ")
                    .append(item.getFoodItem().getPrice())
                    .append("$")
                    .append(" = ")
                    .append(new BigDecimal(item.getQuantity()).multiply(item.getFoodItem().getPrice()))
                    .append("$")
                    .append("\n"));
            return stringBuilder.toString();
        });
        functions.add(orderFunc -> orderFunc.getOrderPrice() + "$");
        functions.add(orderFunc -> {
            PromoCode promoCode = orderFunc.getPromoCode();
            return promoCode != null ? String.valueOf(promoCode.getDiscount()) : String.valueOf(0);
        });

        ExcelBuilderDto<Order> excelBuilderDto = ExcelBuilderDto.<Order>builder()
                .fileName("ORDERS")
                .list(orderList)
                .columnNames(columnNames)
                .cellFunctions(functions)
                .build();

        return excelOrderBuilderService.exportWithExcelBuilder(excelBuilderDto);
    }

    public ResourceDTO exportUsers() {
        List<User> usersList = userRepository.findAll();
        String[] columnNames = {"ID", "Username", "First Name", "Last Name",
                "Email", "Phone number", "Address", "User Role"};
        List<Function<User, String>> functions = new ArrayList<>();
        functions.add(user -> String.valueOf(user.getId()));
        functions.add(User::getUserName);
        functions.add(User::getFirstName);
        functions.add(User::getLastName);
        functions.add(User::getEmail);
        functions.add(User::getPhoneNumber);
        functions.add(User::getAddress);
        functions.add(user -> user.getRole().getRoleName().name());
        ExcelBuilderDto<User> excelBuilderDto = ExcelBuilderDto.<User>builder()
                .fileName("USERS")
                .list(usersList)
                .columnNames(columnNames)
                .cellFunctions(functions)
                .build();

        return excelUserBuilderService.exportWithExcelBuilder(excelBuilderDto);
    }
}
