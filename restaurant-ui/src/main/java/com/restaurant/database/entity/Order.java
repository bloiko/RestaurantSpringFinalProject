package com.restaurant.database.entity;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Order entity.
 *
 * @author B.Loiko
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "food_order")
public class Order implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "order_date")
    private Timestamp orderDate;

    @Column(name = "order_price")
    private BigDecimal orderPrice;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true)
    private List<Item> items;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private OrderStatus orderStatus;

    @OneToOne
    @JoinColumn(name = "promo_code", referencedColumnName = "code", nullable = true)
    private PromoCode promoCode;

    public Order(Long id, Timestamp orderDate, BigDecimal orderPrice, User user, List<Item> items, OrderStatus orderStatus) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderPrice = orderPrice;
        this.user = user;
        this.items = items;
        this.orderStatus = orderStatus;
        this.promoCode = null;
    }
}
