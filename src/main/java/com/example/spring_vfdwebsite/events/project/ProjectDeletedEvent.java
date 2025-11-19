package com.example.spring_vfdwebsite.events.project;

import lombok.Value;

@Value
public class ProjectDeletedEvent {
    private final Integer projectId;
}
