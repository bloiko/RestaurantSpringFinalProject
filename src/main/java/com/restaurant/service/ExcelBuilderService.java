package com.restaurant.service;

import com.restaurant.database.dao.OrderRepository;
import com.restaurant.database.entity.Order;
import com.restaurant.service.dto.ExcelBuilderDto;
import com.restaurant.web.dto.ResourceDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ExcelBuilderService {

    private final OrderRepository orderRepository;


    public ExcelBuilderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public ResourceDTO exportWithExcelBuilder(ExcelBuilderDto excelBuilderDto) {;
        Resource resource = prepareExcel(excelBuilderDto);
        return ResourceDTO.builder().resource(resource).
                mediaType(MediaType.parseMediaType
                        ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).build();
    }

    private Resource prepareExcel(ExcelBuilderDto excelBuilderDto) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(excelBuilderDto.getFileName());

        prepareHeaders(workbook, sheet, excelBuilderDto.getColumnNames());
        populateOrderData(workbook, sheet, excelBuilderDto);

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            workbook.write(byteArrayOutputStream);
            return new ByteArrayResource(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Error while generating excel.");
        }
    }

    private void populateOrderData(Workbook workbook, Sheet sheet,
                                   ExcelBuilderDto excelBuilderDto) {
        int rowNo = 1;
        Font font = workbook.createFont();
        font.setFontName("Arial");

        sheet.autoSizeColumn(0);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);
        for (Order order : excelBuilderDto.getOrderList()) {
            int columnNo = 0;
            Row row = sheet.createRow(rowNo);
            for (Function<Order, String> function : excelBuilderDto.getCellFunctions()) {
                populateCell(sheet, row, columnNo++, function.apply(order), cellStyle);
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
