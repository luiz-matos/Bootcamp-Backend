package com.bootcamp.bootcampbackend.controllers;

import com.bootcamp.bootcampbackend.entities.Bootcamp;
import com.bootcamp.bootcampbackend.services.BootcampService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;

@RestController
@RequestMapping("bootcamps")
public class BootcampController {

    @Autowired
    private BootcampService bootcampService;

    @GetMapping
    public List<Bootcamp> getBootcampList() {
        return bootcampService.getBootcampList();
    }

    @GetMapping("/{id}")
    public Bootcamp getBootcamp(@PathVariable Long id) {
        return bootcampService.getBootcamp(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Bootcamp addBootcamp(@Valid @RequestBody Bootcamp bootcamp) {
        return bootcampService.addBootcamp(bootcamp);
    }

    @PutMapping("/{id}")
    public Bootcamp updateBootcamp(@PathVariable Long id, @Valid @RequestBody Bootcamp bootcamp) {
        return bootcampService.updateBootcamp(id, bootcamp);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBootcamp(@PathVariable Long id) {
        bootcampService.deleteBootcamp(id);
    }

}
