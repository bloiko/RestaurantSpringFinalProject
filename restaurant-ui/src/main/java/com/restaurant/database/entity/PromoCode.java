package com.restaurant.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "promo_code")
public class PromoCode {
    @Id
    @Column(name = "code")
    @NotEmpty(message = "code should not be empty")
    private String code;

    @Column(name = "discount")
    @Min(value = 0, message = "Discount should not be less than 0")
    @Max(value = 50, message = "Discount must not be greater than 50")
    private int discount;

    @Column(name = "active")
    private boolean active;
}
