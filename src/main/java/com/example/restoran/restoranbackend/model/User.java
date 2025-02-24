package com.example.restoran.restoranbackend.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;
    private String firstname;
    private String lastname;
    private int salary;

    @Enumerated(EnumType.STRING)
    private Position position;

    @Enumerated(EnumType.STRING)
    private Role role;


    public void setPosition(Position position) {
        this.position = position;
        this.salary = position.getSalary();
    }
}
