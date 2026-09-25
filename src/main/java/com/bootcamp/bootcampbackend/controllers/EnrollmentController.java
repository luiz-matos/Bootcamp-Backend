package com.bootcamp.bootcampbackend.controllers;

import com.bootcamp.bootcampbackend.dtos.StudentResponse;
import com.bootcamp.bootcampbackend.services.EnrollmentService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bootcamps/{id}/students")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public List<StudentResponse> list(@PathVariable Long id) {
        return enrollmentService.findStudents(id);
    }

    @PostMapping("/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enroll(@PathVariable Long id, @PathVariable Long studentId) {
        enrollmentService.enroll(id, studentId);
    }

    @DeleteMapping("/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unenroll(@PathVariable Long id, @PathVariable Long studentId) {
        enrollmentService.unenroll(id, studentId);
    }
}
