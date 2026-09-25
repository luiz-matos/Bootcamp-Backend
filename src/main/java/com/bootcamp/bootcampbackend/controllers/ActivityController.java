package com.bootcamp.bootcampbackend.controllers;

import com.bootcamp.bootcampbackend.dtos.ActivityRequest;
import com.bootcamp.bootcampbackend.dtos.ActivityResponse;
import com.bootcamp.bootcampbackend.services.ActivityService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bootcamps/{bootcampId}/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public List<ActivityResponse> getActivityList(@PathVariable Long bootcampId) {
        return activityService.getActivityList(bootcampId);
    }

    @GetMapping("/{id}")
    public ActivityResponse getActivity(@PathVariable Long bootcampId, @PathVariable Long id) {
        return activityService.getActivity(bootcampId, id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ActivityResponse addActivity(@PathVariable Long bootcampId, @Valid @RequestBody ActivityRequest request) {
        return activityService.addActivity(bootcampId, request);
    }

    @PutMapping("/{id}")
    public ActivityResponse updateActivity(
            @PathVariable Long bootcampId, @PathVariable Long id, @Valid @RequestBody ActivityRequest request) {
        return activityService.updateActivity(bootcampId, id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActivity(@PathVariable Long bootcampId, @PathVariable Long id) {
        activityService.deleteActivity(bootcampId, id);
    }
}
