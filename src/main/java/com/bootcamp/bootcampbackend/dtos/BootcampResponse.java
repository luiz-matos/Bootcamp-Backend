package com.bootcamp.bootcampbackend.dtos;

import com.bootcamp.bootcampbackend.entities.Bootcamp;
import java.time.LocalDate;

public record BootcampResponse(
        Long id, String name, String description, int creditHours, LocalDate startDate, LocalDate endDate, double xp) {

    public static BootcampResponse from(Bootcamp bootcamp) {
        return new BootcampResponse(
                bootcamp.getId(),
                bootcamp.getName(),
                bootcamp.getDescription(),
                bootcamp.getCreditHours(),
                bootcamp.getStartDate(),
                bootcamp.getEndDate(),
                bootcamp.xpCalculate());
    }
}
