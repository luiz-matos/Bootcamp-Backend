package com.bootcamp.bootcampbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
public record Activity(
        @Id
        Long id,
        @Column
        String title,
        @Column
        String description,
        @Column
        LocalDate dateOfMentoring,
        @ManyToOne
        Bootcamp bootcamp
) {
    private static final double DEFAULT_XP = 15d;

    public double xpCalculate() {
        return DEFAULT_XP + 20d;
    }
}
