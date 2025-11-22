package com.example.spring_vfdwebsite.events.matchSchedule;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class MatchScheduleEventListener {
    @EventListener
    public void handleMatchScheduleUpdatedEvent(MatchScheduleUpdatedEvent event) {

        System.out.println("🔥 MatchScheduleEventListener handleUpdatedEvent");
        System.out.printf("🔥 MatchSchedule with ID %d has been updated: %s%n",
                event.getMatchScheduleId(),
                event.getUpdatedMatchSchedule().getRound());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleMatchScheduleDeletedEvent(MatchScheduleDeletedEvent event) {

        System.out.println("🔥 MatchScheduleEventListener handleMatchScheduleDeletedEvent");
        System.out.printf("🔥 MatchSchedule with ID %d has been deleted", event.getMatchScheduleId());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleMatchScheduleCreatedEvent(MatchScheduleCreatedEvent event) {
        System.out.println("🔥 MatchScheduleEventListener handleMatchScheduleCreatedEvent");
        System.out.printf("🔥 MatchSchedule with ID %d has been created: %s%n",
                event.getMatchScheduleId(),
                event.getCreatedMatchSchedule().getRound());
        // Here you can add additional logic, such as sending notifications,
    }
}
