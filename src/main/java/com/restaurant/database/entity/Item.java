package com.restaurant.database.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Item entity.
 *
 * @author B.Loiko
 *
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="item")
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "food_id", referencedColumnName = "id")
    private FoodItem foodItem;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;
}
