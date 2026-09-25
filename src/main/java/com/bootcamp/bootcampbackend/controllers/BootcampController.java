package com.bootcamp.bootcampbackend.controllers;

import com.bootcamp.bootcampbackend.dtos.BootcampRequest;
import com.bootcamp.bootcampbackend.dtos.BootcampResponse;
import com.bootcamp.bootcampbackend.dtos.StudentResponse;
import com.bootcamp.bootcampbackend.services.BootcampService;
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
@RequestMapping("bootcamps")
public class BootcampController {

    private final BootcampService bootcampService;

    public BootcampController(BootcampService bootcampService) {
        this.bootcampService = bootcampService;
    }

    @GetMapping
    public List<BootcampResponse> getBootcampList() {
        return bootcampService.getBootcampList();
    }

    @GetMapping("/{id}")
    public BootcampResponse getBootcamp(@PathVariable Long id) {
        return bootcampService.getBootcamp(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BootcampResponse addBootcamp(@Valid @RequestBody BootcampRequest request) {
        return bootcampService.addBootcamp(request);
    }

    @PutMapping("/{id}")
    public BootcampResponse updateBootcamp(@PathVariable Long id, @Valid @RequestBody BootcampRequest request) {
        return bootcampService.updateBootcamp(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBootcamp(@PathVariable Long id) {
        bootcampService.deleteBootcamp(id);
    }

    @GetMapping("/{id}/students")
    public List<StudentResponse> getEnrolledStudents(@PathVariable Long id) {
        return bootcampService.getEnrolledStudents(id);
    }

    @PostMapping("/{id}/students/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enrollStudent(@PathVariable Long id, @PathVariable Long studentId) {
        bootcampService.enrollStudent(id, studentId);
    }

    @DeleteMapping("/{id}/students/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unenrollStudent(@PathVariable Long id, @PathVariable Long studentId) {
        bootcampService.unenrollStudent(id, studentId);
    }
}
