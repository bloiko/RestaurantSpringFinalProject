package com.restaurant.service;

import com.restaurant.database.dao.OrderRepository;
import com.restaurant.database.entity.Item;
import com.restaurant.database.entity.Order;
import com.restaurant.service.dto.ExcelBuilderDto;
import com.restaurant.web.dto.ResourceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ExcelService {

    private final ExcelBuilderService excelBuilderService;

    private final OrderRepository orderRepository;

    public ExcelService(ExcelBuilderService excelBuilderService, OrderRepository orderRepository) {
        this.excelBuilderService = excelBuilderService;
        this.orderRepository = orderRepository;
    }


    public ResourceDTO exportMonthOrders(Timestamp startDate, Timestamp endDate) {
        List<Order> orderList = orderRepository.findAllByOrderDateBetween(startDate, endDate);
        String[] columnNames = {"Order ID", "Order Date", "User Full Name", "User Email",
                                "Order Status", "Food Item + Quantity + Price of one Item"};
        List<Function<Order, String>> functions = new ArrayList<>();
        functions.add(orderFunc -> String.valueOf(orderFunc.getId()));
        functions.add(orderFunc -> String.valueOf(orderFunc.getOrderDate()));
        functions.add(orderFunc -> orderFunc.getUser().getFirstName() + " " + orderFunc.getUser().getLastName());
        functions.add(orderFunc -> orderFunc.getUser().getEmail());
        functions.add(orderFunc -> orderFunc.getOrderStatus().getStatusName());
        functions.add(orderFunc -> {
            List<Item> items = orderFunc.getItems();
            StringBuilder stringBuilder = new StringBuilder();
            items.forEach(item -> {
                stringBuilder.append(item.getFoodItem().getName())
                        .append("    ")
                        .append(item.getQuantity())
                        .append(" items * ")
                        .append(item.getFoodItem().getPrice())
                        .append("$")
                        .append(" = ")
                        .append(item.getQuantity() * item.getFoodItem().getPrice())
                        .append("$")
                        .append("\n");
            });
            return stringBuilder.toString();
        });
        ExcelBuilderDto excelBuilderDto = ExcelBuilderDto.builder()
                .fileName("ORDERS")
                .orderList(orderList)
                .columnNames(columnNames)
                .cellFunctions(functions)
                .build();

        return excelBuilderService.exportWithExcelBuilder(excelBuilderDto);
    }
}
