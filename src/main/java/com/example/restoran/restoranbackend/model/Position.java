package com.example.restoran.restoranbackend.model;

import lombok.Getter;


@Getter
public enum Position {
    Cleaner(1000),
    Waiter(1300),
    Chef(1500),
    Manager(3000);

    private final int salary;

    Position(int salary) {
        this.salary = salary;
    }
}
