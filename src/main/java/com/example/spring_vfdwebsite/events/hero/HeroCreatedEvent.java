package com.example.spring_vfdwebsite.events.hero;

import com.example.spring_vfdwebsite.entities.Hero;

import lombok.Value;

@Value
public class HeroCreatedEvent {
    private final Integer heroId;
    private final Hero createdHero;
}
