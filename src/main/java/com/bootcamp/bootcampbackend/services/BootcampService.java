package com.bootcamp.bootcampbackend.services;

import com.bootcamp.bootcampbackend.entities.Bootcamp;
import com.bootcamp.bootcampbackend.exceptions.ConflictException;
import com.bootcamp.bootcampbackend.repositories.BootcampRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BootcampService {

    private final BootcampRepository bootcampRepository;

    public BootcampService(BootcampRepository bootcampRepository) {
        this.bootcampRepository = bootcampRepository;
    }

    public List<Bootcamp> getBootcampList() {
        return bootcampRepository.findAll();
    }

    public Bootcamp addBootcamp(Bootcamp bootcamp) {
        if (bootcampRepository.existsByName(bootcamp.getName())) {
            throw new ConflictException("Já existe um bootcamp com o nome " + bootcamp.getName());
        }
        return bootcampRepository.save(bootcamp);
    }
}
