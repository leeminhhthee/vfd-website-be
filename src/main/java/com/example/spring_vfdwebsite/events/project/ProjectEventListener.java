package com.example.spring_vfdwebsite.events.project;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class ProjectEventListener {
    @EventListener
    public void handleProjectUpdatedEvent(ProjectUpdatedEvent event) {

        System.out.println("🔥 ProjectEventListener handleProjectUpdatedEvent");
        System.out.printf("🔥 Project with ID %d has been updated: %s%n",
                event.getProjectId(),
                event.getUpdatedProject().getTitle());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleProjectDeletedEvent(ProjectDeletedEvent event) {

        System.out.println("🔥 ProjectEventListener handleProjectDeletedEvent");
        System.out.printf("🔥 Project with ID %d has been deleted", event.getProjectId());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleProjectCreatedEvent(ProjectCreatedEvent event) {
        System.out.println("🔥 ProjectEventListener handleProjectCreatedEvent");
        System.out.printf("🔥 Project with ID %d has been created: %s%n",
                event.getProjectId(),
                event.getCreatedProject().getTitle());
        // Here you can add additional logic, such as sending notifications,
    }
}
