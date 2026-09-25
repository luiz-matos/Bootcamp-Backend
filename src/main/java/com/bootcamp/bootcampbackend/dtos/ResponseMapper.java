package com.bootcamp.bootcampbackend.dtos;

import com.bootcamp.bootcampbackend.entities.Activity;
import com.bootcamp.bootcampbackend.entities.Bootcamp;
import com.bootcamp.bootcampbackend.entities.Student;
import com.bootcamp.bootcampbackend.rules.ExperiencePolicy;
import org.springframework.stereotype.Component;

@Component
public class ResponseMapper {

    private final ExperiencePolicy experiencePolicy;

    public ResponseMapper(ExperiencePolicy experiencePolicy) {
        this.experiencePolicy = experiencePolicy;
    }

    public BootcampResponse toResponse(Bootcamp bootcamp) {
        return new BootcampResponse(
                bootcamp.getId(),
                bootcamp.getName(),
                bootcamp.getDescription(),
                bootcamp.getCreditHours(),
                bootcamp.getStartDate(),
                bootcamp.getEndDate(),
                experiencePolicy.xpFor(bootcamp));
    }

    public StudentResponse toResponse(Student student) {
        return new StudentResponse(student.getId(), student.getName(), experiencePolicy.xpFor(student));
    }

    public ActivityResponse toResponse(Activity activity) {
        return new ActivityResponse(
                activity.getId(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getDateOfMentoring(),
                experiencePolicy.xpFor(activity));
    }
}
