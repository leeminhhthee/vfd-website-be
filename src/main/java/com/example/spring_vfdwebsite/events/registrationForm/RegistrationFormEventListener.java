package com.example.spring_vfdwebsite.events.registrationForm;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class RegistrationFormEventListener {
    @EventListener
    public void handleRegistrationFormUpdatedEvent(RegistrationFormUpdatedEvent event) {

        System.out.println("🔥 RegistrationFormEventListener handleUpdatedEvent");
        System.out.printf("🔥 RegistrationForm with ID %d has been updated: %s%n",
                event.getRegistrationFormId(),
                event.getUpdatedRegistrationForm().getFullName());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleRegistrationFormDeletedEvent(RegistrationFormDeletedEvent event) {

        System.out.println("🔥 RegistrationFormEventListener handleRegistrationFormDeletedEvent");
        System.out.printf("🔥 RegistrationForm with ID %d has been deleted", event.getRegistrationFormId());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleRegistrationFormCreatedEvent(RegistrationFormCreatedEvent event) {
        System.out.println("🔥 RegistrationFormEventListener handleRegistrationFormCreatedEvent");
        System.out.printf("🔥 RegistrationForm with ID %d has been created: %s%n",
                event.getRegistrationFormId(),
                event.getCreatedRegistrationForm().getFullName());
        // Here you can add additional logic, such as sending notifications,
    }
}
