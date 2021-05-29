package com.restaurant.database.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Item entity.
 *
 * @author B.Loiko
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="item")
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "food_id", referencedColumnName = "id")
    private FoodItem foodItem;

    @Column(name = "quantity")
    private int quantity;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "order_item",
            joinColumns = {
                    @JoinColumn(name = "item_id", referencedColumnName = "id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "order_id", referencedColumnName = "id",
                            nullable = false, updatable = false)})
    private List<Order> orders;
}
