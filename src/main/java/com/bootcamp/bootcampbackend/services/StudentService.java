package com.bootcamp.bootcampbackend.services;

import com.bootcamp.bootcampbackend.dtos.ResponseMapper;
import com.bootcamp.bootcampbackend.dtos.StudentRequest;
import com.bootcamp.bootcampbackend.dtos.StudentResponse;
import com.bootcamp.bootcampbackend.entities.Bootcamp;
import com.bootcamp.bootcampbackend.entities.Student;
import com.bootcamp.bootcampbackend.exceptions.NotFoundException;
import com.bootcamp.bootcampbackend.repositories.BootcampRepository;
import com.bootcamp.bootcampbackend.repositories.StudentRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final BootcampRepository bootcampRepository;
    private final ResponseMapper responseMapper;

    public StudentService(
            StudentRepository studentRepository, BootcampRepository bootcampRepository, ResponseMapper responseMapper) {
        this.studentRepository = studentRepository;
        this.bootcampRepository = bootcampRepository;
        this.responseMapper = responseMapper;
    }

    public List<StudentResponse> findAll() {
        return studentRepository.findAll().stream()
                .map(responseMapper::toResponse)
                .toList();
    }

    public StudentResponse findById(Long id) {
        return responseMapper.toResponse(getEntity(id));
    }

    public Student getEntity(Long id) {
        return studentRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Aluno " + id + " não encontrado"));
    }

    public StudentResponse create(StudentRequest request) {
        Student student = new Student();
        request.applyTo(student);
        return responseMapper.toResponse(studentRepository.save(student));
    }

    public StudentResponse update(Long id, StudentRequest request) {
        Student student = getEntity(id);
        request.applyTo(student);
        return responseMapper.toResponse(studentRepository.save(student));
    }

    @Transactional
    public void delete(Long id) {
        Student student = getEntity(id);
        for (Bootcamp bootcamp : bootcampRepository.findByStudentsId(id)) {
            bootcamp.getStudents().remove(student);
        }
        studentRepository.delete(student);
    }
}
