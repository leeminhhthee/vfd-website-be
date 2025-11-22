package com.example.spring_vfdwebsite.events.matchSchedule;

import lombok.Value;

@Value
public class MatchScheduleDeletedEvent {
    private final Integer matchScheduleId;
}