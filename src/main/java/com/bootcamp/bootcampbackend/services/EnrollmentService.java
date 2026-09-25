package com.bootcamp.bootcampbackend.services;

import com.bootcamp.bootcampbackend.dtos.ResponseMapper;
import com.bootcamp.bootcampbackend.dtos.StudentResponse;
import com.bootcamp.bootcampbackend.entities.Bootcamp;
import com.bootcamp.bootcampbackend.entities.Student;
import com.bootcamp.bootcampbackend.exceptions.ConflictException;
import com.bootcamp.bootcampbackend.exceptions.NotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnrollmentService {

    private final BootcampService bootcampService;
    private final StudentService studentService;
    private final ResponseMapper responseMapper;

    public EnrollmentService(
            BootcampService bootcampService, StudentService studentService, ResponseMapper responseMapper) {
        this.bootcampService = bootcampService;
        this.studentService = studentService;
        this.responseMapper = responseMapper;
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> findStudents(Long bootcampId) {
        return bootcampService.getEntity(bootcampId).getStudents().stream()
                .map(responseMapper::toResponse)
                .toList();
    }

    @Transactional
    public void enroll(Long bootcampId, Long studentId) {
        Bootcamp bootcamp = bootcampService.getEntity(bootcampId);
        Student student = studentService.getEntity(studentId);
        if (bootcamp.getStudents().contains(student)) {
            throw new ConflictException("O aluno " + studentId + " já está matriculado no bootcamp " + bootcampId);
        }
        bootcamp.getStudents().add(student);
    }

    @Transactional
    public void unenroll(Long bootcampId, Long studentId) {
        Bootcamp bootcamp = bootcampService.getEntity(bootcampId);
        Student student = studentService.getEntity(studentId);
        if (!bootcamp.getStudents().remove(student)) {
            throw new NotFoundException("O aluno " + studentId + " não está matriculado no bootcamp " + bootcampId);
        }
    }
}
