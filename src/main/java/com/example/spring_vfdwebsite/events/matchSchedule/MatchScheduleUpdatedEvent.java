package com.example.spring_vfdwebsite.events.matchSchedule;

import com.example.spring_vfdwebsite.entities.MatchSchedule;

import lombok.Value;

@Value
public class MatchScheduleUpdatedEvent {
    private final Integer matchScheduleId;
    private final MatchSchedule updatedMatchSchedule;
}
