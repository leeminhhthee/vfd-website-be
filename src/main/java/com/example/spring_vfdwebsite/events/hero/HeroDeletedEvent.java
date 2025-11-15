package com.example.spring_vfdwebsite.events.hero;

import lombok.Value;

@Value
public class HeroDeletedEvent {
    private final Integer heroId;
}
