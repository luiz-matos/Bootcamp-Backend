package com.bootcamp.bootcampbackend.dtos;

import java.time.LocalDate;

public record BootcampResponse(
        Long id, String name, String description, int creditHours, LocalDate startDate, LocalDate endDate, double xp) {}
