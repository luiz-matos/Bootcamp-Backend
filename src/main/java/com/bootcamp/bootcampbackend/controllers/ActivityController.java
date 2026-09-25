package com.bootcamp.bootcampbackend.controllers;

import com.bootcamp.bootcampbackend.entities.Activity;
import com.bootcamp.bootcampbackend.services.ActivityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;

@RestController
@RequestMapping("bootcamps/{bootcampId}/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping
    public List<Activity> getActivityList(@PathVariable Long bootcampId) {
        return activityService.getActivityList(bootcampId);
    }

    @GetMapping("/{id}")
    public Activity getActivity(@PathVariable Long bootcampId, @PathVariable Long id) {
        return activityService.getActivity(bootcampId, id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Activity addActivity(@PathVariable Long bootcampId, @Valid @RequestBody Activity activity) {
        return activityService.addActivity(bootcampId, activity);
    }

    @PutMapping("/{id}")
    public Activity updateActivity(@PathVariable Long bootcampId, @PathVariable Long id,
                                   @Valid @RequestBody Activity activity) {
        return activityService.updateActivity(bootcampId, id, activity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActivity(@PathVariable Long bootcampId, @PathVariable Long id) {
        activityService.deleteActivity(bootcampId, id);
    }

}
