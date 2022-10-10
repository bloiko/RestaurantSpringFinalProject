package com.restaurant.database.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * User entity.
 *
 * @author B.Loiko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user")
public class User implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "first_name")
//    @NotEmpty(message = "First name must be filled!")
    private String firstName;

    @Column(name = "last_name")
//    @NotEmpty(message = "Last name must be filled!")
    private String lastName;

    @Column(name = "username")
    @NotEmpty(message = "User name must be filled!")
    private String userName;

    @Column(name = "password")
    @NotEmpty(message = "Password must be filled!")
    private String password;

    @Column(name = "email")
    @NotEmpty(message = "Email must be filled!")
    @Email(message = "Incorrect email format!")
    private String email;

    @Column(name = "address")
//    @NotEmpty(message = "Address must be filled!")
    private String address;

    @Column(name = "phone_number")
//    @NotEmpty(message = "Phone number must be filled!")
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private Role role;

    public User(String username, String password, Role role) {
        this.userName = username;
        this.password = password;
        this.role = role;
    }

    public User(String username, String password, String firstName, String lastName, String email, Role role) {
        this.userName = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }
}
