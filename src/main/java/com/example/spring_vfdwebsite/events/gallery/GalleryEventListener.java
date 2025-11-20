package com.example.spring_vfdwebsite.events.gallery;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class GalleryEventListener {
    @EventListener
    public void handleGalleryUpdatedEvent(GalleryUpdatedEvent event) {

        System.out.println("🔥 GalleryEventListener handleGalleryUpdatedEvent");
        System.out.printf("🔥 Gallery with ID %d has been updated: %s%n",
                event.getGalleryId(),
                event.getUpdatedGallery().getTitle());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleGalleryDeletedEvent(GalleryDeletedEvent event) {

        System.out.println("🔥 GalleryEventListener handleGalleryDeletedEvent");
        System.out.printf("🔥 Gallery with ID %d has been deleted", event.getGalleryId());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleGalleryCreatedEvent(GalleryCreatedEvent event) {
        System.out.println("🔥 GalleryEventListener handleGalleryCreatedEvent");
        System.out.printf("🔥 Gallery with ID %d has been created: %s%n",
                event.getGalleryId(),
                event.getCreatedGallery().getTitle());
        // Here you can add additional logic, such as sending notifications,
    }
}
