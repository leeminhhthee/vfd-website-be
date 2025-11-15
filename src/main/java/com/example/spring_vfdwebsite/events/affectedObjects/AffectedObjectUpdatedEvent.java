package com.example.spring_vfdwebsite.events.affectedObjects;

import com.example.spring_vfdwebsite.entities.AffectedObject;

import lombok.Value;

@Value
public class AffectedObjectUpdatedEvent {
    private final Integer affectedObjectId;
    private final AffectedObject affectedObject;
}
