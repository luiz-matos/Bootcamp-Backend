package com.bootcamp.bootcampbackend.services;

import com.bootcamp.bootcampbackend.entities.Activity;
import com.bootcamp.bootcampbackend.exceptions.NotFoundException;
import com.bootcamp.bootcampbackend.repositories.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final BootcampService bootcampService;

    public ActivityService(ActivityRepository activityRepository, BootcampService bootcampService) {
        this.activityRepository = activityRepository;
        this.bootcampService = bootcampService;
    }

    public List<Activity> getActivityList(Long bootcampId) {
        bootcampService.getBootcamp(bootcampId);
        return activityRepository.findByBootcampId(bootcampId);
    }

    public Activity getActivity(Long bootcampId, Long id) {
        return activityRepository.findByIdAndBootcampId(id, bootcampId)
                .orElseThrow(() -> new NotFoundException(
                        "Atividade " + id + " não encontrada no bootcamp " + bootcampId));
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
        activityRepository.delete(getActivity(bootcampId, id));
    }
}
