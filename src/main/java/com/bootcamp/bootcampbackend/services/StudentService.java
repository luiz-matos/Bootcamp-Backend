package com.bootcamp.bootcampbackend.services;

import com.bootcamp.bootcampbackend.dtos.ActivityResponse;
import com.bootcamp.bootcampbackend.dtos.BootcampResponse;
import com.bootcamp.bootcampbackend.dtos.StudentRequest;
import com.bootcamp.bootcampbackend.dtos.StudentResponse;
import com.bootcamp.bootcampbackend.entities.Activity;
import com.bootcamp.bootcampbackend.entities.Bootcamp;
import com.bootcamp.bootcampbackend.entities.Student;
import com.bootcamp.bootcampbackend.exceptions.ConflictException;
import com.bootcamp.bootcampbackend.exceptions.NotFoundException;
import com.bootcamp.bootcampbackend.repositories.ActivityRepository;
import com.bootcamp.bootcampbackend.repositories.BootcampRepository;
import com.bootcamp.bootcampbackend.repositories.StudentRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final BootcampRepository bootcampRepository;
    private final ActivityRepository activityRepository;

    public StudentService(
            StudentRepository studentRepository,
            BootcampRepository bootcampRepository,
            ActivityRepository activityRepository) {
        this.studentRepository = studentRepository;
        this.bootcampRepository = bootcampRepository;
        this.activityRepository = activityRepository;
    }

    public List<StudentResponse> getStudentList() {
        return studentRepository.findAll().stream().map(StudentResponse::from).toList();
    }

    public StudentResponse getStudent(Long id) {
        return StudentResponse.from(findStudent(id));
    }

    public Student findStudent(Long id) {
        return studentRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Aluno " + id + " não encontrado"));
    }

    public StudentResponse addStudent(StudentRequest request) {
        Student student = new Student();
        request.applyTo(student);
        return StudentResponse.from(studentRepository.save(student));
    }

    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = findStudent(id);
        request.applyTo(student);
        return StudentResponse.from(studentRepository.save(student));
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = findStudent(id);
        for (Bootcamp bootcamp : bootcampRepository.findByStudentsId(id)) {
            bootcamp.getStudents().remove(student);
        }
        studentRepository.delete(student);
    }

    public List<ActivityResponse> getCompletedActivities(Long id) {
        return findStudent(id).getCompletedActivities().stream()
                .map(ActivityResponse::from)
                .toList();
    }

    public List<BootcampResponse> getCompletedBootcamps(Long id) {
        return findStudent(id).getCompletedBootcamps().stream()
                .map(BootcampResponse::from)
                .toList();
    }

    @Transactional
    public void completeActivity(Long id, Long activityId) {
        Student student = findStudent(id);
        Activity activity = activityRepository
                .findById(activityId)
                .orElseThrow(() -> new NotFoundException("Atividade " + activityId + " não encontrada"));
        if (!activity.getBootcamp().getStudents().contains(student)) {
            throw new ConflictException(
                    "O aluno " + id + " não está matriculado no bootcamp da atividade " + activityId);
        }
        if (student.getCompletedActivities().contains(activity)) {
            throw new ConflictException("O aluno " + id + " já concluiu a atividade " + activityId);
        }
        student.getCompletedActivities().add(activity);

        Bootcamp bootcamp = activity.getBootcamp();
        if (student.getCompletedActivities().containsAll(bootcamp.getActivities())) {
            student.getCompletedBootcamps().add(bootcamp);
        }
    }
}
