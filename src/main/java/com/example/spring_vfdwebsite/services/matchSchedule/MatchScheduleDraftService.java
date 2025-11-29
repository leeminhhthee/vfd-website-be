package com.example.spring_vfdwebsite.services.matchSchedule;

import java.util.List;

import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.fillDataDTOs.ApproveDraftsRequestDto;
import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.fillDataDTOs.MatchAiDto;
import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.fillDataDTOs.MatchScheduleDraftResponseDto;
import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.fillDataDTOs.SaveDraftsRequestDto;

public interface MatchScheduleDraftService {

    List<MatchScheduleDraftResponseDto> findAll();

    List<MatchScheduleDraftResponseDto> findByTournament(Integer tournamentId);

    MatchScheduleDraftResponseDto findById(Integer id);

    List<MatchAiDto> extractMatches(String imageUrl);

    void approveDrafts(ApproveDraftsRequestDto dto);
    
    List<MatchScheduleDraftResponseDto> saveDrafts(SaveDraftsRequestDto dto);
}
