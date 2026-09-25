package com.bootcamp.bootcampbackend.controllers;

import com.bootcamp.bootcampbackend.dtos.BootcampRequest;
import com.bootcamp.bootcampbackend.dtos.BootcampResponse;
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
    public List<BootcampResponse> list() {
        return bootcampService.findAll();
    }

    @GetMapping("/{id}")
    public BootcampResponse get(@PathVariable Long id) {
        return bootcampService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BootcampResponse create(@Valid @RequestBody BootcampRequest request) {
        return bootcampService.create(request);
    }

    @PutMapping("/{id}")
    public BootcampResponse update(@PathVariable Long id, @Valid @RequestBody BootcampRequest request) {
        return bootcampService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bootcampService.delete(id);
    }
}
