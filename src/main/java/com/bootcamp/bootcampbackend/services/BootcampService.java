package com.bootcamp.bootcampbackend.services;

import com.bootcamp.bootcampbackend.dtos.BootcampRequest;
import com.bootcamp.bootcampbackend.dtos.BootcampResponse;
import com.bootcamp.bootcampbackend.dtos.ResponseMapper;
import com.bootcamp.bootcampbackend.entities.Bootcamp;
import com.bootcamp.bootcampbackend.exceptions.ConflictException;
import com.bootcamp.bootcampbackend.exceptions.NotFoundException;
import com.bootcamp.bootcampbackend.repositories.BootcampRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BootcampService {

    private final BootcampRepository bootcampRepository;
    private final ResponseMapper responseMapper;

    public BootcampService(BootcampRepository bootcampRepository, ResponseMapper responseMapper) {
        this.bootcampRepository = bootcampRepository;
        this.responseMapper = responseMapper;
    }

    public List<BootcampResponse> findAll() {
        return bootcampRepository.findAll().stream()
                .map(responseMapper::toResponse)
                .toList();
    }

    public BootcampResponse findById(Long id) {
        return responseMapper.toResponse(getEntity(id));
    }

    public Bootcamp getEntity(Long id) {
        return bootcampRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Bootcamp " + id + " não encontrado"));
    }

    public BootcampResponse create(BootcampRequest request) {
        if (bootcampRepository.existsByName(request.name())) {
            throw new ConflictException("Já existe um bootcamp com o nome " + request.name());
        }
        Bootcamp bootcamp = new Bootcamp();
        request.applyTo(bootcamp);
        return responseMapper.toResponse(bootcampRepository.save(bootcamp));
    }

    public BootcampResponse update(Long id, BootcampRequest request) {
        Bootcamp bootcamp = getEntity(id);
        if (!bootcamp.getName().equals(request.name()) && bootcampRepository.existsByName(request.name())) {
            throw new ConflictException("Já existe um bootcamp com o nome " + request.name());
        }
        request.applyTo(bootcamp);
        return responseMapper.toResponse(bootcampRepository.save(bootcamp));
    }

    @Transactional
    public void delete(Long id) {
        Bootcamp bootcamp = getEntity(id);
        if (!bootcamp.getStudents().isEmpty() || !bootcamp.getActivities().isEmpty()) {
            throw new ConflictException("O bootcamp tem alunos matriculados ou atividades e não pode ser excluído");
        }
        bootcampRepository.delete(bootcamp);
    }
}
