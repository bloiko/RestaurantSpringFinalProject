package com.restaurant.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromoCodeDto {
    private boolean exists;

    private String code;

    private int discount;
}
