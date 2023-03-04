package com.restaurant.service;

import com.restaurant.database.dao.OrderRepository;
import com.restaurant.database.entity.Item;
import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.User;
import com.restaurant.web.dto.ResourceDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;


import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ExcelService {

    private final OrderRepository orderRepository;


    public ExcelService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public ResourceDTO exportMonthOrders() {
        List<Order> orderList = orderRepository.findAll();
        Resource resource = prepareExcel(orderList);
        return ResourceDTO.builder().resource(resource).
                mediaType(MediaType.parseMediaType
                        ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).build();
    }

    private Resource prepareExcel(List<Order> orderList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("ORDERS");

        //TODO add column with food_item + quantity
        prepareHeaders(workbook, sheet, "Order ID", "Order Date", "User Full Name", "User Email",
                "Order Status", "Food Item + Quantity + Price of one Item");
        populateOrderData(workbook, sheet, orderList);

        try (ByteArrayOutputStream byteArrayOutputStream
                     = new ByteArrayOutputStream()) {

            workbook.write(byteArrayOutputStream);
            return new
                    ByteArrayResource
                    (byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException
                    ("Error while generating excel.");
        }
    }

    private void populateOrderData(Workbook workbook, Sheet sheet,
                                   List<Order> orderList) {

        int rowNo = 1;
        Font font = workbook.createFont();
        font.setFontName("Arial");


        sheet.autoSizeColumn(0);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);
        for (Order order : orderList) {
            int columnNo = 0;
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

            Row row = sheet.createRow(rowNo);
            for (Function<Order, String> function : functions) {
                populateCell(sheet, row, columnNo++,
                        function.apply(order), cellStyle);
            }
            rowNo++;
        }
    }

    private void populateCell(Sheet sheet, Row row, int columnNo,
                              String value, CellStyle cellStyle) {

        Cell cell = row.createCell(columnNo);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(value);
        sheet.autoSizeColumn(columnNo);
    }

    private void prepareHeaders(Workbook workbook,
                                Sheet sheet, String... headers) {

        Row headerRow = sheet.createRow(0);
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontName("Arial");

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        int columnNo = 0;
        for (String header : headers) {
            Cell headerCell = headerRow.createCell(columnNo++);
            headerCell.setCellValue(header);
            headerCell.setCellStyle(cellStyle);
        }
    }
}
