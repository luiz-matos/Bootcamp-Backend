package com.bootcamp.bootcampbackend.services;

import com.bootcamp.bootcampbackend.entities.Activity;
import com.bootcamp.bootcampbackend.entities.Bootcamp;
import com.bootcamp.bootcampbackend.entities.Student;
import com.bootcamp.bootcampbackend.exceptions.ConflictException;
import com.bootcamp.bootcampbackend.exceptions.NotFoundException;
import com.bootcamp.bootcampbackend.repositories.ActivityRepository;
import com.bootcamp.bootcampbackend.repositories.BootcampRepository;
import com.bootcamp.bootcampbackend.repositories.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final BootcampRepository bootcampRepository;
    private final ActivityRepository activityRepository;

    public StudentService(StudentRepository studentRepository, BootcampRepository bootcampRepository,
                          ActivityRepository activityRepository) {
        this.studentRepository = studentRepository;
        this.bootcampRepository = bootcampRepository;
        this.activityRepository = activityRepository;
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

    public List<Activity> getCompletedActivities(Long id) {
        return getStudent(id).getCompletedActivities();
    }

    @Transactional
    public void completeActivity(Long id, Long activityId) {
        Student student = getStudent(id);
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException("Atividade " + activityId + " não encontrada"));
        if (!activity.getBootcamp().getStudents().contains(student)) {
            throw new ConflictException(
                    "O aluno " + id + " não está matriculado no bootcamp da atividade " + activityId);
        }
        if (student.getCompletedActivities().contains(activity)) {
            throw new ConflictException("O aluno " + id + " já concluiu a atividade " + activityId);
        }
        student.getCompletedActivities().add(activity);
    }

}
