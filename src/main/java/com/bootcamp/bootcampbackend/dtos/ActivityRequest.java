package com.bootcamp.bootcampbackend.dtos;

import com.bootcamp.bootcampbackend.entities.Activity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ActivityRequest(
        @NotBlank(message = "O título é obrigatório") String title,
        String description,

        @NotNull(message = "A data da mentoria é obrigatória")
        LocalDate dateOfMentoring) {

    public void applyTo(Activity activity) {
        activity.setTitle(title);
        activity.setDescription(description);
        activity.setDateOfMentoring(dateOfMentoring);
    }
}
