package com.restaurant.service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.function.Function;

@Data
@Builder
public class ExcelBuilderDto<T> {

    private String fileName;

    private List<T> list;

    private String[] columnNames;

    private List<Function<T, String>> cellFunctions;
}
