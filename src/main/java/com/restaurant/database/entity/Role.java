package com.restaurant.database.entity;

import lombok.AllArgsConstructor;
import javax.persistence.*;



@AllArgsConstructor
@Table(name = "role")
public enum Role {
    USER("USER"),
    ADMIN("ADMIN");
    @Column(name = "name")
    private String name;
}
