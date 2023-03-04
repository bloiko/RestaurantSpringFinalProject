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
import java.util.Date;
import java.util.List;

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

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        for (Order order : orderList) {
            int columnNo = 0;
            Row row = sheet.createRow(rowNo);
            populateCell(sheet, row, columnNo++,
                    String.valueOf(order.getId()), cellStyle);
            populateCell(sheet, row, columnNo++,
                    String.valueOf(order.getOrderDate()), cellStyle);
            User orderUser = order.getUser();
            populateCell(sheet, row, columnNo++,
                    orderUser.getFirstName() + " " + orderUser.getLastName(), cellStyle);
            populateCell(sheet, row, columnNo++,
                    orderUser.getEmail(), cellStyle);
            populateCell(sheet, row, columnNo++,
                    order.getOrderStatus().getStatusName(), cellStyle);

            List<Item> items = order.getItems();
            StringBuilder stringBuilder = new StringBuilder();
            items.forEach(item -> {
                stringBuilder.append(item.getFoodItem().getName())
                             .append("    ")
                             .append(item.getQuantity())
                             .append(" items * ")
                             .append(item.getFoodItem().getPrice())
                             .append("$")
                             .append(" = ")
                             .append(order.getOrderPrice())
                             .append("$")
                             .append("\n");
            });
            if(stringBuilder.length() > 1) {
                stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length() - 1);
            }
            sheet.autoSizeColumn(0);
            cellStyle.setWrapText(true);
            populateCell(sheet, row, columnNo++,
                    stringBuilder.toString(), cellStyle);
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
