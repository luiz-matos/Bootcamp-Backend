package com.bootcamp.bootcampbackend.services;

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

    public ActivityService(
            ActivityRepository activityRepository,
            StudentRepository studentRepository,
            BootcampService bootcampService) {
        this.activityRepository = activityRepository;
        this.studentRepository = studentRepository;
        this.bootcampService = bootcampService;
    }

    public List<Activity> getActivityList(Long bootcampId) {
        bootcampService.getBootcamp(bootcampId);
        return activityRepository.findByBootcampId(bootcampId);
    }

    public Activity getActivity(Long bootcampId, Long id) {
        return activityRepository
                .findByIdAndBootcampId(id, bootcampId)
                .orElseThrow(
                        () -> new NotFoundException("Atividade " + id + " não encontrada no bootcamp " + bootcampId));
    }

    public Activity addActivity(Long bootcampId, Activity activity) {
        activity.setBootcamp(bootcampService.getBootcamp(bootcampId));
        return activityRepository.save(activity);
    }

    public Activity updateActivity(Long bootcampId, Long id, Activity data) {
        Activity activity = getActivity(bootcampId, id);
        activity.setTitle(data.getTitle());
        activity.setDescription(data.getDescription());
        activity.setDateOfMentoring(data.getDateOfMentoring());
        return activityRepository.save(activity);
    }

    public void deleteActivity(Long bootcampId, Long id) {
        Activity activity = getActivity(bootcampId, id);
        if (studentRepository.existsByCompletedActivitiesId(id)) {
            throw new ConflictException("A atividade " + id + " já foi concluída por alunos e não pode ser excluída");
        }
        activityRepository.delete(activity);
    }
}
