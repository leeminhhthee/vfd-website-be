package com.example.spring_vfdwebsite.events.affectedObjects;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class AffectedObjectEventListener {
    @EventListener
    public void handleAffectedObjectUpdatedEvent(AffectedObjectUpdatedEvent event) {

        System.out.println("🔥 AffectedObjectEventListener handleAffectedObjectUpdatedEvent");
        System.out.printf("🔥 AffectedObject with ID %d has been updated: %s%n",
                event.getAffectedObjectId(),
                event.getAffectedObject().getTitle());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleAffectedObjectDeletedEvent(AffectedObjectDeteledEvent event) {

        System.out.println("🔥 AffectedObjectEventListener handleAffectedObjectDeletedEvent");
        System.out.printf("🔥 AffectedObject with ID %d has been deleted", event.getAffectedObjectId());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleAffectedObjectCreatedEvent(AffectedObjectCreatedEvent event) {
        System.out.println("🔥 AffectedObjectEventListener handleAffectedObjectCreatedEvent");
        System.out.printf("🔥 AffectedObject with ID %d has been created: %s%n",
                event.getAffectedObjectId(),
                event.getCreatedAffectedObject().getTitle());
        // Here you can add additional logic, such as sending notifications,
    }
}
