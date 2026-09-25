package com.bootcamp.bootcampbackend.controllers;

import com.bootcamp.bootcampbackend.dtos.ActivityResponse;
import com.bootcamp.bootcampbackend.dtos.BootcampResponse;
import com.bootcamp.bootcampbackend.dtos.StudentRequest;
import com.bootcamp.bootcampbackend.dtos.StudentResponse;
import com.bootcamp.bootcampbackend.services.StudentService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<StudentResponse> getStudentList() {
        return studentService.getStudentList();
    }

    @GetMapping("/{id}")
    public StudentResponse getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse addStudent(@Valid @RequestBody StudentRequest request) {
        return studentService.addStudent(request);
    }

    @PutMapping("/{id}")
    public StudentResponse updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return studentService.updateStudent(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("/{id}/completed-activities")
    public List<ActivityResponse> getCompletedActivities(@PathVariable Long id) {
        return studentService.getCompletedActivities(id);
    }

    @GetMapping("/{id}/completed-bootcamps")
    public List<BootcampResponse> getCompletedBootcamps(@PathVariable Long id) {
        return studentService.getCompletedBootcamps(id);
    }

    @PostMapping("/{id}/completed-activities/{activityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void completeActivity(@PathVariable Long id, @PathVariable Long activityId) {
        studentService.completeActivity(id, activityId);
    }
}
