package com.bootcamp.bootcampbackend.services;

import com.bootcamp.bootcampbackend.dtos.ActivityResponse;
import com.bootcamp.bootcampbackend.dtos.BootcampResponse;
import com.bootcamp.bootcampbackend.dtos.ResponseMapper;
import com.bootcamp.bootcampbackend.entities.Activity;
import com.bootcamp.bootcampbackend.entities.Bootcamp;
import com.bootcamp.bootcampbackend.entities.Student;
import com.bootcamp.bootcampbackend.exceptions.ConflictException;
import com.bootcamp.bootcampbackend.exceptions.NotFoundException;
import com.bootcamp.bootcampbackend.repositories.ActivityRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompletionService {

    private final StudentService studentService;
    private final ActivityRepository activityRepository;
    private final ResponseMapper responseMapper;

    public CompletionService(
            StudentService studentService, ActivityRepository activityRepository, ResponseMapper responseMapper) {
        this.studentService = studentService;
        this.activityRepository = activityRepository;
        this.responseMapper = responseMapper;
    }

    @Transactional(readOnly = true)
    public List<ActivityResponse> findCompletedActivities(Long studentId) {
        return studentService.getEntity(studentId).getCompletedActivities().stream()
                .map(responseMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BootcampResponse> findCompletedBootcamps(Long studentId) {
        return studentService.getEntity(studentId).getCompletedBootcamps().stream()
                .map(responseMapper::toResponse)
                .toList();
    }

    @Transactional
    public void completeActivity(Long studentId, Long activityId) {
        Student student = studentService.getEntity(studentId);
        Activity activity = activityRepository
                .findById(activityId)
                .orElseThrow(() -> new NotFoundException("Atividade " + activityId + " não encontrada"));
        Bootcamp bootcamp = activity.getBootcamp();
        if (!bootcamp.getStudents().contains(student)) {
            throw new ConflictException(
                    "O aluno " + studentId + " não está matriculado no bootcamp da atividade " + activityId);
        }
        if (student.getCompletedActivities().contains(activity)) {
            throw new ConflictException("O aluno " + studentId + " já concluiu a atividade " + activityId);
        }
        student.getCompletedActivities().add(activity);

        if (student.getCompletedActivities().containsAll(bootcamp.getActivities())) {
            student.getCompletedBootcamps().add(bootcamp);
        }
    }
}
