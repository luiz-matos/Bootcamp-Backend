package com.bootcamp.bootcampbackend.dtos;

import com.bootcamp.bootcampbackend.entities.Activity;
import java.time.LocalDate;

public record ActivityResponse(Long id, String title, String description, LocalDate dateOfMentoring, double xp) {

    public static ActivityResponse from(Activity activity) {
        return new ActivityResponse(
                activity.getId(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getDateOfMentoring(),
                activity.xpCalculate());
    }
}
