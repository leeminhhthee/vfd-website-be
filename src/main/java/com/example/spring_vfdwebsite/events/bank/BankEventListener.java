package com.example.spring_vfdwebsite.events.bank;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BankEventListener {
    @EventListener
    public void handleBankUpdatedEvent(BankUpdatedEvent event) {

        System.out.println("🔥 BankEventListener handleBankUpdatedEvent");
        System.out.printf("🔥 Bank with ID %d has been updated: %s%n",
                event.getBankId(),
                event.getUpdatedBank().getFullName());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleBankDeletedEvent(BankDeletedEvent event) {

        System.out.println("🔥 BankEventListener handleBankDeletedEvent");
        System.out.printf("🔥 Bank with ID %d has been deleted", event.getBankId());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleBankCreatedEvent(BankCreatedEvent event) {
        System.out.println("🔥 BankEventListener handleBankCreatedEvent");
        System.out.printf("🔥 Bank with ID %d has been created: %s%n",
                event.getBankId(),
                event.getCreatedBank().getFullName());
        // Here you can add additional logic, such as sending notifications,
    }
}
