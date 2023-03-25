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
    private String statusName;
 /*   public static OrderStatus getOrderStatus(String value){
        if(value.equals(WAITING.value)){
            return WAITING;
        }else if(value.equals(PREPARING.value)){
            return PREPARING;
        }else if(value.equals(READY.value)){
            return READY;
        }else if(value.equals(DELIVERED.value)){
            return DELIVERED;
        }else if(value.equals(DONE.value)){
            return DONE;
        }
        return DONE;
    }*/
}
