package com.example.spring_vfdwebsite.services.matchSchedule;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_vfdwebsite.annotations.LoggableAction;
import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.fillDataDTOs.ApproveDraftsRequestDto;
import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.fillDataDTOs.MatchAiDto;
import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.fillDataDTOs.MatchScheduleDraftResponseDto;
import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.fillDataDTOs.SaveDraftsRequestDto;
import com.example.spring_vfdwebsite.entities.MatchSchedule;
import com.example.spring_vfdwebsite.entities.MatchScheduleDraft;
import com.example.spring_vfdwebsite.entities.Tournament;
import com.example.spring_vfdwebsite.entities.enums.DraftStatusEnum;
import com.example.spring_vfdwebsite.entities.enums.RoundEnum;
import com.example.spring_vfdwebsite.repositories.MatchScheduleDraftJpaRepository;
import com.example.spring_vfdwebsite.repositories.MatchScheduleJpaRepository;
import com.example.spring_vfdwebsite.repositories.TournamentJpaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchScheduleDraftServiceImpl implements MatchScheduleDraftService {

    private final MatchScheduleDraftJpaRepository matchScheduleDraftJpaRepository;
    private final MatchScheduleJpaRepository matchScheduleRepository;
    // private final OpenRouterService openRouterService;
    private final GeminiOcrService geminiOcrService;
    private final TournamentJpaRepository tournamentRepository;

    // ==================== Get All =====================
    @Override
    @Cacheable(value = "match-schedule-drafts", key = "'all'")
    @Transactional(readOnly = true)
    public List<MatchScheduleDraftResponseDto> findAll() {
        System.out.println("Fetching all match schedule drafts from database...");
        return matchScheduleDraftJpaRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    // ==================== Get By Tournament =====================
    @Override
    @Cacheable(value = "match-schedule-drafts", key = "'tournament-' + #tournamentId")
    @Transactional(readOnly = true)
    public List<MatchScheduleDraftResponseDto> findByTournament(Integer tournamentId) {
        System.out.println("Fetching match schedule drafts for tournament " + tournamentId + " from database...");
        return matchScheduleDraftJpaRepository.findByTournament_Id(tournamentId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // ==================== Get By Id =====================
    @Override
    @Cacheable(value = "match-schedule-drafts", key = "#root.args[0]")
    @Transactional(readOnly = true)
    public MatchScheduleDraftResponseDto findById(Integer id) {
        System.out.println("Fetching match schedule draft with id " + id + " from database...");
        return matchScheduleDraftJpaRepository.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    // ==================== Extract Matches =====================
    @Override
    @Transactional(readOnly = true)
    @LoggableAction(value =  "EXTRACT_MATCHES", entity = "match-schedule-drafts", description = "Extract matches from image URL")
    public List<MatchAiDto> extractMatches(String imageUrl) {
        // return openRouterService.extractMatchesFromImageUrl(imageUrl);
        return geminiOcrService.extractScheduleFromImageUrl(imageUrl);
    }

    // ==================== Save Drafts (New) =====================
    @Override
    @Transactional
    @CacheEvict(value = "match-schedule-drafts", allEntries = true) // Xóa cache để hiện data mới
    @LoggableAction(value =  "SAVE_DRAFTS", entity = "match-schedule-drafts", description = "Save new match schedule drafts")
    public List<MatchScheduleDraftResponseDto> saveDrafts(SaveDraftsRequestDto dto) {
        // 1. Kiểm tra giải đấu có tồn tại không
        Tournament tournament = tournamentRepository.findById(dto.getTournamentId())
                .orElseThrow(() -> new RuntimeException("Tournament not found with id: " + dto.getTournamentId()));

        List<MatchScheduleDraft> draftsToSave = new ArrayList<>();

        // 2. Map từ DTO (AI Data) sang Entity
        for (MatchAiDto item : dto.getMatches()) {
            MatchScheduleDraft draft = MatchScheduleDraft.builder()
                    .tournament(tournament)
                    .teamA(item.getTeamA())
                    .teamB(item.getTeamB())
                    .groupTable(item.getGroupTable())
                    .round(mapToRoundEnum(item.getRound())) // Xử lý Enum
                    .matchDate(item.getMatchDate()) // Xử lý Ngày Giờ
                    .status(DraftStatusEnum.PENDING) // Mặc định là PENDING
                    .build();
            
            draftsToSave.add(draft);
        }

        // 3. Lưu Batch vào DB
        List<MatchScheduleDraft> savedDrafts = matchScheduleDraftJpaRepository.saveAll(draftsToSave);
        log.info("Saved {} new drafts for tournament {}", savedDrafts.size(), tournament.getId());

        // 4. Trả về kết quả
        return savedDrafts.stream().map(this::toDto).toList();
    }

    // --- Helper: Xử lý Enum Vòng đấu ---
    private RoundEnum mapToRoundEnum(String roundStr) {
        if (roundStr == null) return RoundEnum.GROUP;
        String lower = roundStr.toLowerCase();
        
        if (lower.contains("chung kết")) return RoundEnum.FINAL;
        if (lower.contains("hạng ba") || lower.contains("tranh hạng ba")) return RoundEnum.THIRD_PLACE;
        if (lower.contains("bán kết")) return RoundEnum.SEMI_FINAL;
        if (lower.contains("tứ kết")) return RoundEnum.QUARTER_FINAL;
        
        return RoundEnum.GROUP; // Mặc định
    }

    // ==================== Approve Drafts =====================
    @Override
    @Transactional
    @CacheEvict(value = "match-schedule-drafts", allEntries = true)
    @LoggableAction(value =  "APPROVE_DRAFTS", entity = "match-schedule-drafts", description = "Approve match schedule drafts and create official matches")
    public void approveDrafts(ApproveDraftsRequestDto dto) {
        List<Integer> ids = dto.getId();
        if (ids == null || ids.isEmpty()) return;

        List<MatchScheduleDraft> drafts = matchScheduleDraftJpaRepository.findAllById(ids);

        if (drafts.isEmpty()) {
            throw new RuntimeException("No drafts found");
        }

        List<MatchSchedule> newMatches = new ArrayList<>();
        List<MatchScheduleDraft> draftsToDelete = new ArrayList<>();

        for (MatchScheduleDraft draft : drafts) {

            if (draft.getStatus() != DraftStatusEnum.PENDING) {
                continue; 
            }

            MatchSchedule match = MatchSchedule.builder()
                    .round(draft.getRound())
                    .groupTable(draft.getGroupTable())
                    .matchDate(draft.getMatchDate())
                    .teamA(draft.getTeamA())
                    .teamB(draft.getTeamB())
                    .scoreA(null)
                    .scoreB(null)
                    .tournament(draft.getTournament())
                    .build();

            newMatches.add(match);

            draft.setStatus(DraftStatusEnum.APPROVED);
            draftsToDelete.add(draft);
        }
        if (!newMatches.isEmpty()) {
            matchScheduleRepository.saveAll(newMatches);
            matchScheduleDraftJpaRepository.deleteAll(draftsToDelete);
            log.info("Approved and DELETED {} drafts. Created {} official matches.", 
                      draftsToDelete.size(), newMatches.size());
        }
    }

    // ===================== Mapping -> Dto =====================
    private MatchScheduleDraftResponseDto toDto(MatchScheduleDraft draft) {
        MatchScheduleDraftResponseDto.TournamentInfo tournamentInfo = null;

        if (draft.getTournament() != null) {
            tournamentInfo = MatchScheduleDraftResponseDto.TournamentInfo.builder()
                    .id(draft.getTournament().getId())
                    .name(draft.getTournament().getName())
                    .build();
        }

        return MatchScheduleDraftResponseDto.builder()
                .id(draft.getId())
                .round(draft.getRound())
                .groupTable(draft.getGroupTable())
                .matchDate(draft.getMatchDate())
                .teamA(draft.getTeamA())
                .teamB(draft.getTeamB())
                .status(draft.getStatus())
                // .scheduleImageUrl(draft.getScheduleImageUrl())
                .tournament(tournamentInfo)
                .createdAt(draft.getCreatedAt())
                .updatedAt(draft.getUpdatedAt())
                .build();
    }
}
