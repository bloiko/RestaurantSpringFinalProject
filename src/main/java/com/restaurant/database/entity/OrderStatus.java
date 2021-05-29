package com.restaurant.database.entity;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Order status entity.
 *
 * @author B.Loiko
 *
 */

@Entity
@Table(name="status")
public enum OrderStatus {
    WAITING(1L,"WAITING","В очікуванні"),
    PREPARING(2L,"PREPARING","Готується"),
    READY(3L,"READY","Готове"),
    DELIVERED(4L,"DELIVERED","Доставляється"),
    DONE(5L,"DONE","Доставлено");
    @Id
    private Long id;

    @Column(name = "status_name")
    private String value;
    @Column(name = "status_name_ua")
    private String valueUa;

    OrderStatus(Long id, String value, String valueUa) {
        this.id = id;
        this.value = value;
        this.valueUa = valueUa;
    }
    public String getValue() {
        return value;
    }

    public String getValueUa() {
        return valueUa;
    }

    public static OrderStatus getOrderStatus(String value){
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
    }

    public boolean equalsTo(String name) {
        return value.equals(name);
    }

    public String value() {
        return value;
    }
    public String valueUa() {
        return valueUa;
    }
    public Long getId() {
        return id;
    }
}
