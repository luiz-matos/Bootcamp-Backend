package com.bootcamp.bootcampbackend.services;

import com.bootcamp.bootcampbackend.repositories.ActivityRepository;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }
}
