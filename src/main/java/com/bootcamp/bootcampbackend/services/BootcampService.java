package com.bootcamp.bootcampbackend.services;

import com.bootcamp.bootcampbackend.entities.Bootcamp;
import com.bootcamp.bootcampbackend.exceptions.ConflictException;
import com.bootcamp.bootcampbackend.exceptions.NotFoundException;
import com.bootcamp.bootcampbackend.repositories.BootcampRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Bootcamp getBootcamp(Long id) {
        return bootcampRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Bootcamp " + id + " não encontrado"));
    }

    public Bootcamp addBootcamp(Bootcamp bootcamp) {
        if (bootcampRepository.existsByName(bootcamp.getName())) {
            throw new ConflictException("Já existe um bootcamp com o nome " + bootcamp.getName());
        }
        return bootcampRepository.save(bootcamp);
    }

    public Bootcamp updateBootcamp(Long id, Bootcamp data) {
        Bootcamp bootcamp = getBootcamp(id);
        if (!bootcamp.getName().equals(data.getName()) && bootcampRepository.existsByName(data.getName())) {
            throw new ConflictException("Já existe um bootcamp com o nome " + data.getName());
        }

        bootcamp.setName(data.getName());
        bootcamp.setDescription(data.getDescription());
        bootcamp.setCreditHours(data.getCreditHours());
        bootcamp.setStartDate(data.getStartDate());
        bootcamp.setEndDate(data.getEndDate());
        return bootcampRepository.save(bootcamp);
    }

    @Transactional
    public void deleteBootcamp(Long id) {
        Bootcamp bootcamp = getBootcamp(id);
        if (!bootcamp.getStudents().isEmpty() || !bootcamp.getActivities().isEmpty()) {
            throw new ConflictException("O bootcamp tem alunos matriculados ou atividades e não pode ser excluído");
        }
        bootcampRepository.delete(bootcamp);
    }
}
