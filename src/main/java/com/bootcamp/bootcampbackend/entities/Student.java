package com.bootcamp.bootcampbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public record Student(
        @Id
        int id,
        @Column
        String name,
        @ManyToOne
        Bootcamp bootcamp
) {
}
