package com.restaurant.service.dto;

import com.restaurant.database.entity.Order;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.function.Function;

@Data
@Builder
public class ExcelBuilderDto {

    private String fileName;

    private List<Order> orderList;

    private String[] columnNames;

    private List<Function<Order, String>> cellFunctions;
}
