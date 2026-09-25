package com.bootcamp.bootcampbackend.services;

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

    public List<Bootcamp> getBootcampList() {
        return bootcampRepository.findAll();
    }

    public Bootcamp getBootcamp(Long id) {
        return bootcampRepository
                .findById(id)
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

    @Transactional(readOnly = true)
    public List<Student> getEnrolledStudents(Long id) {
        return List.copyOf(getBootcamp(id).getStudents());
    }

    @Transactional
    public void enrollStudent(Long id, Long studentId) {
        Bootcamp bootcamp = getBootcamp(id);
        Student student = studentService.getStudent(studentId);
        if (bootcamp.getStudents().contains(student)) {
            throw new ConflictException("O aluno " + studentId + " já está matriculado no bootcamp " + id);
        }
        bootcamp.getStudents().add(student);
    }

    @Transactional
    public void unenrollStudent(Long id, Long studentId) {
        Bootcamp bootcamp = getBootcamp(id);
        Student student = studentService.getStudent(studentId);
        if (!bootcamp.getStudents().remove(student)) {
            throw new NotFoundException("O aluno " + studentId + " não está matriculado no bootcamp " + id);
        }
    }
}
