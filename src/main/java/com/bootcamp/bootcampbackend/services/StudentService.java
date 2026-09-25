package com.bootcamp.bootcampbackend.services;

import com.bootcamp.bootcampbackend.entities.Bootcamp;
import com.bootcamp.bootcampbackend.entities.Student;
import com.bootcamp.bootcampbackend.exceptions.NotFoundException;
import com.bootcamp.bootcampbackend.repositories.BootcampRepository;
import com.bootcamp.bootcampbackend.repositories.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final BootcampRepository bootcampRepository;

    public StudentService(StudentRepository studentRepository, BootcampRepository bootcampRepository) {
        this.studentRepository = studentRepository;
        this.bootcampRepository = bootcampRepository;
    }

    public List<Student> getStudentList() {
        return studentRepository.findAll();
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Aluno " + id + " não encontrado"));
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student data) {
        Student student = getStudent(id);
        student.setName(data.getName());
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = getStudent(id);
        for (Bootcamp bootcamp : bootcampRepository.findByStudentsId(id)) {
            bootcamp.getStudents().remove(student);
        }
        studentRepository.delete(student);
    }

}
