package com.example.spring_vfdwebsite.events.users;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {
    @EventListener
    public void handleUserUpdatedEvent(UserUpdatedEvent event) {

        System.out.println("🔥 UserEventListener handleUserUpdatedEvent");
        System.out.printf("🔥 User with ID %d has been updated: %s%n",
                event.getUserId(),
                event.getUpdatedUser().getFullName());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleUserDeletedEvent(UserDeletedEvent event) {

        System.out.println("🔥 UserEventListener handleUserDeletedEvent");
        System.out.printf("🔥 User with ID %d has been deleted", event.getUserId());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        System.out.println("🔥 UserEventListener handleUserCreatedEvent");
        System.out.printf("🔥 User with ID %d has been created: %s%n",
                event.getUserId(),
                event.getCreatedUser().getFullName());
        // Here you can add additional logic, such as sending notifications,
    }
}
