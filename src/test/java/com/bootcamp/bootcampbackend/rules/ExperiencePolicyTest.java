package com.bootcamp.bootcampbackend.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bootcamp.bootcampbackend.entities.Activity;
import com.bootcamp.bootcampbackend.entities.Bootcamp;
import com.bootcamp.bootcampbackend.entities.Student;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ExperiencePolicyTest {

    private final ExperiencePolicy policy = new ExperiencePolicy();

    @Test
    void activityIsWorth35() {
        assertEquals(35, policy.xpFor(new Activity()));
    }

    @Test
    void bootcampIsWorth15PerCreditHour() {
        assertEquals(600, policy.xpFor(bootcamp(40)));
        assertEquals(150, policy.xpFor(bootcamp(10)));
    }

    @Test
    void studentWithoutCompletionsHasNoXp() {
        assertEquals(0, policy.xpFor(new Student()));
    }

    @Test
    void studentXpSumsActivitiesAndFinishedBootcamps() {
        var student = new Student();
        student.setCompletedActivities(List.of(new Activity(), new Activity()));
        student.setCompletedBootcamps(Set.of(bootcamp(40)));

        assertEquals(670, policy.xpFor(student));
    }

    private Bootcamp bootcamp(int creditHours) {
        var bootcamp = new Bootcamp();
        bootcamp.setCreditHours(creditHours);
        return bootcamp;
    }
}
