package com.bootcamp.bootcampbackend.services;

import com.bootcamp.bootcampbackend.dtos.BootcampRequest;
import com.bootcamp.bootcampbackend.dtos.BootcampResponse;
import com.bootcamp.bootcampbackend.dtos.StudentResponse;
import com.bootcamp.bootcampbackend.entities.Bootcamp;
import com.bootcamp.bootcampbackend.entities.Student;
import com.bootcamp.bootcampbackend.exceptions.ConflictException;
import com.bootcamp.bootcampbackend.exceptions.NotFoundException;
import com.bootcamp.bootcampbackend.repositories.BootcampRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BootcampService {

    private final BootcampRepository bootcampRepository;
    private final StudentService studentService;

    public BootcampService(BootcampRepository bootcampRepository, StudentService studentService) {
        this.bootcampRepository = bootcampRepository;
        this.studentService = studentService;
    }

    public List<BootcampResponse> getBootcampList() {
        return bootcampRepository.findAll().stream().map(BootcampResponse::from).toList();
    }

    public BootcampResponse getBootcamp(Long id) {
        return BootcampResponse.from(findBootcamp(id));
    }

    public Bootcamp findBootcamp(Long id) {
        return bootcampRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Bootcamp " + id + " não encontrado"));
    }

    public BootcampResponse addBootcamp(BootcampRequest request) {
        if (bootcampRepository.existsByName(request.name())) {
            throw new ConflictException("Já existe um bootcamp com o nome " + request.name());
        }
        Bootcamp bootcamp = new Bootcamp();
        request.applyTo(bootcamp);
        return BootcampResponse.from(bootcampRepository.save(bootcamp));
    }

    public BootcampResponse updateBootcamp(Long id, BootcampRequest request) {
        Bootcamp bootcamp = findBootcamp(id);
        if (!bootcamp.getName().equals(request.name()) && bootcampRepository.existsByName(request.name())) {
            throw new ConflictException("Já existe um bootcamp com o nome " + request.name());
        }
        request.applyTo(bootcamp);
        return BootcampResponse.from(bootcampRepository.save(bootcamp));
    }

    @Transactional
    public void deleteBootcamp(Long id) {
        Bootcamp bootcamp = findBootcamp(id);
        if (!bootcamp.getStudents().isEmpty() || !bootcamp.getActivities().isEmpty()) {
            throw new ConflictException("O bootcamp tem alunos matriculados ou atividades e não pode ser excluído");
        }
        bootcampRepository.delete(bootcamp);
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getEnrolledStudents(Long id) {
        return findBootcamp(id).getStudents().stream()
                .map(StudentResponse::from)
                .toList();
    }

    @Transactional
    public void enrollStudent(Long id, Long studentId) {
        Bootcamp bootcamp = findBootcamp(id);
        Student student = studentService.findStudent(studentId);
        if (bootcamp.getStudents().contains(student)) {
            throw new ConflictException("O aluno " + studentId + " já está matriculado no bootcamp " + id);
        }
        bootcamp.getStudents().add(student);
    }

    @Transactional
    public void unenrollStudent(Long id, Long studentId) {
        Bootcamp bootcamp = findBootcamp(id);
        Student student = studentService.findStudent(studentId);
        if (!bootcamp.getStudents().remove(student)) {
            throw new NotFoundException("O aluno " + studentId + " não está matriculado no bootcamp " + id);
        }
    }
}
