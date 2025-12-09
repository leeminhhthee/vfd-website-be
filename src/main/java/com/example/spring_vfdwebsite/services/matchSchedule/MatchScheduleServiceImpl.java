package com.example.spring_vfdwebsite.services.matchSchedule;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    @LoggableAction(value = "CREATE", entity = "match-schedules", description = "Create a new match schedule")
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

    // ===================== Create Multiple =====================
    @Override
    @Transactional
    @CacheEvict(value = { "match-schedules", "tournaments" }, allEntries = true)
    @LoggableAction(value = "CREATE_MULTIPLE", entity = "match-schedules", description = "Create multiple new match schedules")
    public List<MatchScheduleResponseDto> createMultipleMatchSchedules(List<MatchScheduleCreateRequestDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }
        // Lấy tournament từ dto đầu tiên
        Integer tournamentId = dtos.get(0).getTournamentId();
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Tournament with id " + tournamentId + " not found"));

        List<MatchSchedule> matchSchedules = dtos.stream()
                .map(dto -> MatchSchedule.builder()
                        .round(dto.getRound())
                        .groupTable(dto.getGroupTable())
                        .matchDate(dto.getMatchDate())
                        .teamA(dto.getTeamA())
                        .teamB(dto.getTeamB())
                        .scoreA(dto.getScoreA())
                        .scoreB(dto.getScoreB())
                        .tournament(tournament)
                        .build())
                .toList();

        // Lưu tất cả cùng lúc
        List<MatchSchedule> savedMatches = matchScheduleRepository.saveAll(matchSchedules);

        // Gửi event cho từng trận đã lưu
        savedMatches.forEach(match -> eventPublisher.publishEvent(
                new MatchScheduleCreatedEvent(match.getId(), match)));

        // Trả về DTO
        return savedMatches.stream()
                .map(this::toDto)
                .toList();
    }

    // ===================== Update =====================
    @Override
    @Transactional
    @CachePut(value = "match-schedules", key = "#p0")
    @CacheEvict(value = { "match-schedules", "tournaments" }, allEntries = true)
    @LoggableAction(value = "UPDATE", entity = "match-schedules", description = "Update an existing match schedule")
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

    // ==================== Update Multiple =====================
    @Override
    @Transactional
    @CacheEvict(value = { "match-schedules", "tournaments" }, allEntries = true)
    @LoggableAction(value = "UPDATE", entity = "match-schedules", description = "Update multiple match schedules")
    public List<MatchScheduleResponseDto> updateMultipleMatchSchedules(List<MatchScheduleUpdateRequestDto> dtos) {

        if (dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }

        // Lấy danh sách id
        List<Integer> ids = dtos.stream()
                .map(MatchScheduleUpdateRequestDto::getId)
                .toList();

        // Load tất cả MatchSchedule 1 lần
        List<MatchSchedule> matchSchedules = matchScheduleRepository.findAllById(ids);

        if (matchSchedules.size() != ids.size()) {
            throw new EntityNotFoundException("Some MatchSchedules were not found");
        }

        // Map id -> entity
        Map<Integer, MatchSchedule> matchMap = matchSchedules.stream()
                .collect(Collectors.toMap(MatchSchedule::getId, m -> m));

        // Update từng entity
        for (MatchScheduleUpdateRequestDto dto : dtos) {

            MatchSchedule match = matchMap.get(dto.getId());

            if (dto.getRound() != null) {
                match.setRound(dto.getRound());
            }
            if (dto.getGroupTable() != null) {
                match.setGroupTable(dto.getGroupTable());
            }
            if (dto.getMatchDate() != null) {
                match.setMatchDate(dto.getMatchDate());
            }
            if (dto.getTeamA() != null) {
                match.setTeamA(dto.getTeamA());
            }
            if (dto.getTeamB() != null) {
                match.setTeamB(dto.getTeamB());
            }
            if (dto.getScoreA() != null) {
                match.setScoreA(dto.getScoreA());
            }
            if (dto.getScoreB() != null) {
                match.setScoreB(dto.getScoreB());
            }
        }

        // Save 1 lần duy nhất
        List<MatchSchedule> updatedMatches = matchScheduleRepository.saveAll(matchSchedules);

        // Publish event
        updatedMatches.forEach(match -> eventPublisher.publishEvent(
                new MatchScheduleUpdatedEvent(match.getId(), match)));

        return updatedMatches.stream()
                .map(this::toDto)
                .toList();
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
    @LoggableAction(value = "DELETE", entity = "match-schedules", description = "Delete an existing match schedule")
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
