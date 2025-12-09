package com.example.spring_vfdwebsite.services.matchSchedule;

import java.util.List;

import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.MatchScheduleCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.MatchScheduleResponseDto;
import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.MatchScheduleUpdateRequestDto;

public interface MatchScheduleService {

    MatchScheduleResponseDto createMatchSchedule(MatchScheduleCreateRequestDto dto);

    // Create Multiple Match Schedules
    List<MatchScheduleResponseDto> createMultipleMatchSchedules(List<MatchScheduleCreateRequestDto> dtos);

    MatchScheduleResponseDto updateMatchSchedule(Integer id, MatchScheduleUpdateRequestDto dto);

    // Update Multiple Match Schedules
    List<MatchScheduleResponseDto> updateMultipleMatchSchedules(List<MatchScheduleUpdateRequestDto> dtos);

    MatchScheduleResponseDto getMatchScheduleById(Integer id);

    List<MatchScheduleResponseDto> getAllMatchSchedules();

    void deleteMatchSchedule(Integer id);

    List<MatchScheduleResponseDto> getMatchSchedulesByTournament(Integer tournamentId);
}
