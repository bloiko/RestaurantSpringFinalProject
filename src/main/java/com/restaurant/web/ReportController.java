package com.restaurant.web;

import com.restaurant.service.ExcelService;
import com.restaurant.web.dto.ResourceDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    private ExcelService excelService;

    public ReportController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @GetMapping("/orders/month")
    public ResponseEntity<Resource> exportMonthOrders() {
        ResourceDTO resourceDTO = excelService.exportMonthOrders();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition",
                "attachment; filename=" + "Order.xlsx");

        return ResponseEntity.ok()
                .contentType(resourceDTO.getMediaType())
                .headers(httpHeaders)
                .body(resourceDTO.getResource());
    }
}
