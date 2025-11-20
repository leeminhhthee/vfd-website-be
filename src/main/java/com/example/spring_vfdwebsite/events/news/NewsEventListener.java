package com.example.spring_vfdwebsite.events.news;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class NewsEventListener {
    @EventListener
    public void handleNewsUpdatedEvent(NewsUpdatedEvent event) {

        System.out.println("🔥 NewsEventListener handleNewsUpdatedEvent");
        System.out.printf("🔥 News with ID %d has been updated: %s%n",
                event.getNewsId(),
                event.getUpdatedNews().getTitle());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleNewsDeletedEvent(NewsDeletedEvent event) {

        System.out.println("🔥 NewsEventListener handleNewsDeletedEvent");
        System.out.printf("🔥 News with ID %d has been deleted", event.getNewsId());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleNewsCreatedEvent(NewsCreatedEvent event) {
        System.out.println("🔥 NewsEventListener handleNewsCreatedEvent");
        System.out.printf("🔥 News with ID %d has been created: %s%n",
                event.getNewsId(),
                event.getCreatedNews().getTitle());
        // Here you can add additional logic, such as sending notifications,
    }
}
