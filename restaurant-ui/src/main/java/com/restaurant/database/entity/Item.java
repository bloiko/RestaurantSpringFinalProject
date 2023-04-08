package com.restaurant.database.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Item entity.
 *
 * @author B.Loiko
 */
@Data
@Builder
@ToString(exclude = "order")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item")
public class Item implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "food_id", referencedColumnName = "id")
    private FoodItem foodItem;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}
