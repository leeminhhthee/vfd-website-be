package com.example.spring_vfdwebsite.events.tournament;

import lombok.Value;

@Value
public class TournamentDeletedEvent {
    private final Integer tournamentId;
}