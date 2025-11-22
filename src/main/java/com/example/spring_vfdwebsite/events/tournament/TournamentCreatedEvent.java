package com.example.spring_vfdwebsite.events.tournament;

import com.example.spring_vfdwebsite.entities.Tournament;

import lombok.Value;

@Value
public class TournamentCreatedEvent {
    private final Integer tournamentId;
    private final Tournament createdTournament;
}