package com.bootcamp.bootcampbackend.controllers;

import com.bootcamp.bootcampbackend.dtos.ActivityResponse;
import com.bootcamp.bootcampbackend.dtos.BootcampResponse;
import com.bootcamp.bootcampbackend.services.CompletionService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("students/{id}")
public class CompletionController {

    private final CompletionService completionService;

    public CompletionController(CompletionService completionService) {
        this.completionService = completionService;
    }

    @GetMapping("/completed-activities")
    public List<ActivityResponse> listActivities(@PathVariable Long id) {
        return completionService.findCompletedActivities(id);
    }

    @PostMapping("/completed-activities/{activityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void completeActivity(@PathVariable Long id, @PathVariable Long activityId) {
        completionService.completeActivity(id, activityId);
    }

    @GetMapping("/completed-bootcamps")
    public List<BootcampResponse> listBootcamps(@PathVariable Long id) {
        return completionService.findCompletedBootcamps(id);
    }
}
