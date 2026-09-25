package com.bootcamp.bootcampbackend.rules;

import com.bootcamp.bootcampbackend.entities.Activity;
import com.bootcamp.bootcampbackend.entities.Bootcamp;
import com.bootcamp.bootcampbackend.entities.Student;
import org.springframework.stereotype.Component;

@Component
public class ExperiencePolicy {

    static final double ACTIVITY_XP = 35;
    static final double XP_PER_CREDIT_HOUR = 15;

    public double xpFor(Activity activity) {
        return ACTIVITY_XP;
    }

    public double xpFor(Bootcamp bootcamp) {
        return XP_PER_CREDIT_HOUR * bootcamp.getCreditHours();
    }

    public double xpFor(Student student) {
        return student.getCompletedActivities().stream()
                        .mapToDouble(this::xpFor)
                        .sum()
                + student.getCompletedBootcamps().stream()
                        .mapToDouble(this::xpFor)
                        .sum();
    }
}
