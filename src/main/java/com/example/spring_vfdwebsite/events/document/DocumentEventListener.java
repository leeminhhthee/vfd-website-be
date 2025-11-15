package com.example.spring_vfdwebsite.events.document;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DocumentEventListener {
    @EventListener
    public void handleDocumentCreated(DocumentCreatedEvent event) {
        System.out.println("🔥 DocumentEventListener handleDocumentCreated");
        System.out.printf("🔥 Document with ID %d has been created: %s%n",
                event.getDocumentId(),
                event.getDocument().getTitle());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleDocumentUpdated(DocumentUpdatedEvent event) {

        System.out.println("🔥 DocumentEventListener handleDocumentUpdated");
        System.out.printf("🔥 Document with ID %d has been updated: %s%n",
                event.getDocumentId(),
                event.getDocument().getTitle());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleDocumentDeleted(DocumentDeletedEvent event) {
        System.out.println("🔥 DocumentEventListener handleDocumentDeleted");
        System.out.printf("🔥 Document with ID %d has been deleted%n",
                event.getDocumentId());
        // Here you can add additional logic, such as sending notifications,
    }
}