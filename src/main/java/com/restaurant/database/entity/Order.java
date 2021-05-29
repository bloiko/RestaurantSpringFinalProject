package com.restaurant.database.entity;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
/**
 * Order entity.
 *
 * @author B.Loiko
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="food_order")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="order_date")
    private Timestamp orderDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany(mappedBy = "orders", fetch = FetchType.LAZY)
    private List<Item> items;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public void addFoodItem(Item item){
        items.add(item);
    }
}
