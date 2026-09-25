package com.bootcamp.bootcampbackend.dtos;

import com.bootcamp.bootcampbackend.entities.Student;

public record StudentResponse(Long id, String name, double xp) {

    public static StudentResponse from(Student student) {
        return new StudentResponse(student.getId(), student.getName(), student.getXp());
    }
}
