package com.restaurant.web;

import com.restaurant.service.ExcelService;
import com.restaurant.web.dto.ResourceDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ExcelService excelService;

    public ReportController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @GetMapping("/orders/month")
    public ResponseEntity<Resource> exportMonthOrders(@RequestParam(required = false) Long startDate, @RequestParam(required = false) Long endDate) {
        if (startDate == null) {
            Date date = Date.from(LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            startDate = date.getTime();
        }
        if (endDate == null) {
            endDate = new Date().getTime();
        }
        ResourceDTO resourceDTO = excelService.exportMonthOrders(new Timestamp(startDate), new Timestamp(endDate));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "attachment; filename=" + "Order.xlsx");

        return ResponseEntity.ok()
                .contentType(resourceDTO.getMediaType())
                .headers(httpHeaders)
                .body(resourceDTO.getResource());
    }

    @GetMapping("/users")
    public ResponseEntity<Resource> exportMonthOrders() {
        ResourceDTO resourceDTO = excelService.exportUsers();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "attachment; filename=" + "Users.xlsx");

        return ResponseEntity.ok()
                .contentType(resourceDTO.getMediaType())
                .headers(httpHeaders)
                .body(resourceDTO.getResource());
    }
}
