package com.example.spring_vfdwebsite.events.hero;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class HeroEventListener {
    @EventListener
    public void handleHeroUpdatedEvent(HeroUpdatedEvent event) {

        System.out.println("🔥 HeroEventListener handleHeroUpdatedEvent");
        System.out.printf("🔥 Hero with ID %d has been updated: %s%n",
                event.getHeroId(),
                event.getUpdatedHero().getTitle());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleHeroDeletedEvent(HeroDeletedEvent event) {

        System.out.println("🔥 HeroEventListener handleHeroDeletedEvent");
        System.out.printf("🔥 Hero with ID %d has been deleted", event.getHeroId());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleHeroCreatedEvent(HeroCreatedEvent event) {
        System.out.println("🔥 HeroEventListener handleHeroCreatedEvent");
        System.out.printf("🔥 Hero with ID %d has been created: %s%n",
                event.getHeroId(),
                event.getCreatedHero().getTitle());
        // Here you can add additional logic, such as sending notifications,
    }
}
