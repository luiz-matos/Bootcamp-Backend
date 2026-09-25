package com.bootcamp.bootcampbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;

@Entity
public record Bootcamp(
        @Id
        int id,
        @Column(unique = true, nullable = false)
        String name,
        @Lob
        String description,
        @Column(nullable = false)
        int creditHours,
        @Column(nullable = false)
        LocalDate startDate,
        @Column(nullable = false)
        LocalDate endDate,
        @OneToMany
        List<Student> students,
        @OneToMany
        List<Activity> activities
) {
}
