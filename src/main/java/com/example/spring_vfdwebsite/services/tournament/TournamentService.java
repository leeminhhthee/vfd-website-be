package com.example.spring_vfdwebsite.services.tournament;

import java.util.List;

import com.example.spring_vfdwebsite.dtos.tournamentDTOs.TournamentCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.tournamentDTOs.TournamentResponseDto;
import com.example.spring_vfdwebsite.dtos.tournamentDTOs.TournamentUpdateRequestDto;

public interface TournamentService {
    TournamentResponseDto createTournament(TournamentCreateRequestDto dto);

    TournamentResponseDto updateTournament(Integer id, TournamentUpdateRequestDto dto);

    TournamentResponseDto getTournamentById(Integer id);

    List<TournamentResponseDto> getAllTournaments();

    void deleteTournament(Integer id);
}
