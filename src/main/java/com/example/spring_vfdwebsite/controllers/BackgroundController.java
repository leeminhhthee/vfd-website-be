package com.example.spring_vfdwebsite.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_vfdwebsite.services.BackgroundService;

@RestController
public class BackgroundController {
    private final BackgroundService backgroundService;

    public BackgroundController(BackgroundService backgroundService) {
        this.backgroundService = backgroundService;
    }

    @GetMapping("/start-task")
    public String startBackgroundTask() {
        backgroundService.performBackgroundTask();
        return "Task is being processed in the background.";
    }
}
