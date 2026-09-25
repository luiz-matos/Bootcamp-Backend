package com.bootcamp.bootcampbackend.services;

import com.bootcamp.bootcampbackend.entities.Bootcamp;
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

    public void addBootcamp(Bootcamp bootcamp) {
        bootcampRepository.save(bootcamp);
    }
}
