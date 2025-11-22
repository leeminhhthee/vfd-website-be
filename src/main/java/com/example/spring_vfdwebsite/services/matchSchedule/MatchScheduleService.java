package com.example.spring_vfdwebsite.services.matchSchedule;

import java.util.List;

import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.MatchScheduleCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.MatchScheduleResponseDto;
import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.MatchScheduleUpdateRequestDto;

public interface MatchScheduleService {
    MatchScheduleResponseDto createMatchSchedule(MatchScheduleCreateRequestDto dto);

    MatchScheduleResponseDto updateMatchSchedule(Integer id, MatchScheduleUpdateRequestDto dto);

    MatchScheduleResponseDto getMatchScheduleById(Integer id);

    List<MatchScheduleResponseDto> getAllMatchSchedules();

    void deleteMatchSchedule(Integer id);
}
