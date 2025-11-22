package com.example.spring_vfdwebsite.events.tournament;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class TournamentEventListener {
    @EventListener
    public void handleTournamentUpdatedEvent(TournamentUpdatedEvent event) {

        System.out.println("🔥 TournamentEventListener handleUpdatedEvent");
        System.out.printf("🔥 Tournament with ID %d has been updated: %s%n",
                event.getTournamentId(),
                event.getUpdatedTournament().getName());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleTournamentDeletedEvent(TournamentDeletedEvent event) {

        System.out.println("🔥 TournamentEventListener handleTournamentDeletedEvent");
        System.out.printf("🔥 Tournament with ID %d has been deleted", event.getTournamentId());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleTournamentCreatedEvent(TournamentCreatedEvent event) {
        System.out.println("🔥 TournamentEventListener handleTournamentCreatedEvent");
        System.out.printf("🔥 Tournament with ID %d has been created: %s%n",
                event.getTournamentId(),
                event.getCreatedTournament().getName());
        // Here you can add additional logic, such as sending notifications,
    }
}
