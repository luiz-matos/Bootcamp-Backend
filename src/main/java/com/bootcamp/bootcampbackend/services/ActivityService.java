package com.bootcamp.bootcampbackend.services;

import com.bootcamp.bootcampbackend.dtos.ActivityRequest;
import com.bootcamp.bootcampbackend.dtos.ActivityResponse;
import com.bootcamp.bootcampbackend.dtos.ResponseMapper;
import com.bootcamp.bootcampbackend.entities.Activity;
import com.bootcamp.bootcampbackend.exceptions.ConflictException;
import com.bootcamp.bootcampbackend.exceptions.NotFoundException;
import com.bootcamp.bootcampbackend.repositories.ActivityRepository;
import com.bootcamp.bootcampbackend.repositories.StudentRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final StudentRepository studentRepository;
    private final BootcampService bootcampService;
    private final ResponseMapper responseMapper;

    public ActivityService(
            ActivityRepository activityRepository,
            StudentRepository studentRepository,
            BootcampService bootcampService,
            ResponseMapper responseMapper) {
        this.activityRepository = activityRepository;
        this.studentRepository = studentRepository;
        this.bootcampService = bootcampService;
        this.responseMapper = responseMapper;
    }

    public List<ActivityResponse> findAll(Long bootcampId) {
        bootcampService.getEntity(bootcampId);
        return activityRepository.findByBootcampId(bootcampId).stream()
                .map(responseMapper::toResponse)
                .toList();
    }

    public ActivityResponse findById(Long bootcampId, Long id) {
        return responseMapper.toResponse(getEntity(bootcampId, id));
    }

    public ActivityResponse create(Long bootcampId, ActivityRequest request) {
        Activity activity = new Activity();
        request.applyTo(activity);
        activity.setBootcamp(bootcampService.getEntity(bootcampId));
        return responseMapper.toResponse(activityRepository.save(activity));
    }

    public ActivityResponse update(Long bootcampId, Long id, ActivityRequest request) {
        Activity activity = getEntity(bootcampId, id);
        request.applyTo(activity);
        return responseMapper.toResponse(activityRepository.save(activity));
    }

    public void delete(Long bootcampId, Long id) {
        Activity activity = getEntity(bootcampId, id);
        if (studentRepository.existsByCompletedActivitiesId(id)) {
            throw new ConflictException("A atividade " + id + " já foi concluída por alunos e não pode ser excluída");
        }
        activityRepository.delete(activity);
    }

    private Activity getEntity(Long bootcampId, Long id) {
        return activityRepository
                .findByIdAndBootcampId(id, bootcampId)
                .orElseThrow(
                        () -> new NotFoundException("Atividade " + id + " não encontrada no bootcamp " + bootcampId));
    }
}
