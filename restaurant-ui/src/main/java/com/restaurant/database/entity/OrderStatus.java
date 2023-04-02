package com.restaurant.database.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Order status entity.
 *
 * @author B.Loiko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "status")
public class OrderStatus {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "status_name")
    @Enumerated(EnumType.STRING)
    private Status statusName;

}
