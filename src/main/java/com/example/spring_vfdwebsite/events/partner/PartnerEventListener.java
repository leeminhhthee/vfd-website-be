package com.example.spring_vfdwebsite.events.partner;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PartnerEventListener {
    @EventListener
    public void handlePartnerUpdatedEvent(PartnerUpdatedEvent event) {

        System.out.println("🔥 PartnerEventListener handlePartnerUpdatedEvent");
        System.out.printf("🔥 Partner with ID %d has been updated: %s%n",
                event.getPartnerId(),
                event.getUpdatedPartner().getName());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handlePartnerDeletedEvent(PartnerDeletedEvent event) {

        System.out.println("🔥 PartnerEventListener handlePartnerDeletedEvent");
        System.out.printf("🔥 Partner with ID %d has been deleted", event.getPartnerId());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handlePartnerCreatedEvent(PartnerCreatedEvent event) {
        System.out.println("🔥 PartnerEventListener handlePartnerCreatedEvent");
        System.out.printf("🔥 Partner with ID %d has been created: %s%n",
                event.getPartnerId(),
                event.getCreatedPartner().getName());
        // Here you can add additional logic, such as sending notifications,
    }
}
