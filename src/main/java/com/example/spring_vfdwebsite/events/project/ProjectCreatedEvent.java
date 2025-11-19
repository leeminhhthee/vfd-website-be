package com.example.spring_vfdwebsite.events.project;

import com.example.spring_vfdwebsite.entities.Project;

import lombok.Value;

@Value
public class ProjectCreatedEvent {
    private final Integer projectId;
    private final Project createdProject;
}
