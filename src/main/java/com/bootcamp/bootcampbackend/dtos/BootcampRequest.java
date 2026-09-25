package com.bootcamp.bootcampbackend.dtos;

import com.bootcamp.bootcampbackend.entities.Bootcamp;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record BootcampRequest(
        @NotBlank(message = "O nome é obrigatório") String name,
        String description,

        @NotNull(message = "A carga horária deve ser maior que zero")
        @Positive(message = "A carga horária deve ser maior que zero")
        Integer creditHours,

        @NotNull(message = "A data de início é obrigatória") LocalDate startDate,

        @NotNull(message = "A data de término é obrigatória")
        LocalDate endDate) {

    @AssertTrue(message = "A data de término não pode ser anterior à de início")
    public boolean isEndDateAfterStartDate() {
        return startDate == null || endDate == null || !endDate.isBefore(startDate);
    }

    public void applyTo(Bootcamp bootcamp) {
        bootcamp.setName(name);
        bootcamp.setDescription(description);
        bootcamp.setCreditHours(creditHours);
        bootcamp.setStartDate(startDate);
        bootcamp.setEndDate(endDate);
    }
}
