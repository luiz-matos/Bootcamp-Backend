package com.bootcamp.bootcampbackend.dtos;

import com.bootcamp.bootcampbackend.entities.Student;
import jakarta.validation.constraints.NotBlank;

public record StudentRequest(
        @NotBlank(message = "O nome é obrigatório") String name) {

    public void applyTo(Student student) {
        student.setName(name);
    }
}
