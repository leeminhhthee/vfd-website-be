package com.example.spring_vfdwebsite.services.matchSchedule;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_vfdwebsite.annotations.LoggableAction;
import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.MatchScheduleCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.MatchScheduleResponseDto;
import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.MatchScheduleUpdateRequestDto;
import com.example.spring_vfdwebsite.entities.MatchSchedule;
import com.example.spring_vfdwebsite.entities.Tournament;
import com.example.spring_vfdwebsite.events.matchSchedule.MatchScheduleCreatedEvent;
import com.example.spring_vfdwebsite.events.matchSchedule.MatchScheduleDeletedEvent;
import com.example.spring_vfdwebsite.events.matchSchedule.MatchScheduleUpdatedEvent;
import com.example.spring_vfdwebsite.exceptions.EntityNotFoundException;
import com.example.spring_vfdwebsite.repositories.MatchScheduleJpaRepository;
import com.example.spring_vfdwebsite.repositories.TournamentJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchScheduleServiceImpl implements MatchScheduleService {

    private final MatchScheduleJpaRepository matchScheduleRepository;
    private final TournamentJpaRepository tournamentRepository;
    private final ApplicationEventPublisher eventPublisher;

    // ===================== Create =====================
    @Override
    @Transactional
    @CacheEvict(value = { "match-schedules", "tournaments" }, allEntries = true)
    @LoggableAction(value =  "CREATE", entity = "match-schedules", description = "Create a new match schedule")
    public MatchScheduleResponseDto createMatchSchedule(MatchScheduleCreateRequestDto dto) {
        Tournament tournament = tournamentRepository.findById(dto.getTournamentId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Tournament with id " + dto.getTournamentId() + " not found"));

        MatchSchedule matchSchedule = MatchSchedule.builder()
                .round(dto.getRound())
                .groupTable(dto.getGroupTable())
                .matchDate(dto.getMatchDate())
                .teamA(dto.getTeamA())
                .teamB(dto.getTeamB())
                .scoreA(dto.getScoreA())
                .scoreB(dto.getScoreB())
                .tournament(tournament)
                .build();

        MatchSchedule matchSave = matchScheduleRepository.save(matchSchedule);

        eventPublisher.publishEvent(new MatchScheduleCreatedEvent(matchSave.getId(), matchSave));

        return toDto(matchSave);
    }

    // ===================== Update =====================
    @Override
    @Transactional
    @CachePut(value = "match-schedules", key = "#p0")
    @CacheEvict(value = { "match-schedules", "tournaments" }, allEntries = true)
    @LoggableAction(value =  "UPDATE", entity = "match-schedules", description = "Update an existing match schedule")
    public MatchScheduleResponseDto updateMatchSchedule(Integer id, MatchScheduleUpdateRequestDto dto) {
        MatchSchedule matchSchedule = matchScheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MatchSchedule with id " + id + " not found"));

        if (dto.getRound() != null) {
            matchSchedule.setRound(dto.getRound());
        }
        if (dto.getGroupTable() != null) {
            matchSchedule.setGroupTable(dto.getGroupTable());
        }
        if (dto.getMatchDate() != null) {
            matchSchedule.setMatchDate(dto.getMatchDate());
        }
        if (dto.getTeamA() != null) {
            matchSchedule.setTeamA(dto.getTeamA());
        }
        if (dto.getTeamB() != null) {
            matchSchedule.setTeamB(dto.getTeamB());
        }
        if (dto.getScoreA() != null) {
            matchSchedule.setScoreA(dto.getScoreA());
        }
        if (dto.getScoreB() != null) {
            matchSchedule.setScoreB(dto.getScoreB());
        }

        matchSchedule = matchScheduleRepository.save(matchSchedule);

        eventPublisher.publishEvent(new MatchScheduleUpdatedEvent(matchSchedule.getId(), matchSchedule));

        return toDto(matchSchedule);
    }

    // ===================== Get By Id =====================
    @Override
    @Cacheable(value = "match-schedules", key = "#root.args[0]")
    @Transactional(readOnly = true)
    public MatchScheduleResponseDto getMatchScheduleById(Integer id) {
        MatchSchedule matchSchedule = matchScheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MatchSchedule with id " + id + " not found"));
        return toDto(matchSchedule);
    }

    // ===================== Get All =====================
    @Override
    @Cacheable(value = "match-schedules", key = "'all'")
    @Transactional(readOnly = true)
    public List<MatchScheduleResponseDto> getAllMatchSchedules() {
        System.out.println("🔥 Fetching all match schedules from the database...");
        return matchScheduleRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ===================== Delete =====================
    @Override
    @Transactional
    @CacheEvict(value = { "match-schedules", "tournaments" }, allEntries = true)
    @LoggableAction(value =  "DELETE", entity = "match-schedules", description = "Delete an existing match schedule")
    public void deleteMatchSchedule(Integer id) {
        MatchSchedule matchSchedule = matchScheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MatchSchedule with id " + id + " not found"));
        matchScheduleRepository.delete(matchSchedule);
        eventPublisher.publishEvent(new MatchScheduleDeletedEvent(id));
    }

    // ===================== Get By Tournament =====================
    @Override
    @Cacheable(value = "match-schedules", key = "'tournament_' + #root.args[0]")
    @Transactional(readOnly = true)
    public List<MatchScheduleResponseDto> getMatchSchedulesByTournament(Integer tournamentId) {
        tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Tournament with id " + tournamentId + " not found"));
        return matchScheduleRepository.findByTournament_Id(tournamentId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ===================== Mapping -> Dto =====================
    private MatchScheduleResponseDto toDto(MatchSchedule matchSchedule) {
        MatchScheduleResponseDto.TournamentInfo tournamentInfoDto = MatchScheduleResponseDto.TournamentInfo.builder()
                .id(matchSchedule.getTournament().getId())
                .name(matchSchedule.getTournament().getName())
                .build();

        return MatchScheduleResponseDto.builder()
                .id(matchSchedule.getId())
                .round(matchSchedule.getRound())
                .groupTable(matchSchedule.getGroupTable())
                .matchDate(matchSchedule.getMatchDate())
                .teamA(matchSchedule.getTeamA())
                .teamB(matchSchedule.getTeamB())
                .scoreA(matchSchedule.getScoreA())
                .scoreB(matchSchedule.getScoreB())
                .tournament(tournamentInfoDto)
                .createdAt(matchSchedule.getCreatedAt())
                .updatedAt(matchSchedule.getUpdatedAt())
                .build();
    }

}
