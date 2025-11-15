package com.example.spring_vfdwebsite.events.affectedObjects;

import lombok.Value;

@Value
public class AffectedObjectDeteledEvent {
    private final Integer affectedObjectId;
}
