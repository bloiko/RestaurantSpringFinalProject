package com.restaurant.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {

    @Id
    @Column(name = "role_id")
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    @Override
    public String getAuthority() {
        return roleName.name();
    }
}
