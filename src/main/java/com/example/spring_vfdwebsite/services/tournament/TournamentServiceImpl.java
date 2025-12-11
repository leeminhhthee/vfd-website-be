package com.example.spring_vfdwebsite.services.tournament;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_vfdwebsite.annotations.LoggableAction;
import com.example.spring_vfdwebsite.dtos.tournamentDTOs.TournamentCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.tournamentDTOs.TournamentUpdateRequestDto;
import com.example.spring_vfdwebsite.dtos.tournamentDTOs.TournamentResponseDto;
import com.example.spring_vfdwebsite.entities.Document;
import com.example.spring_vfdwebsite.entities.Tournament;
import com.example.spring_vfdwebsite.entities.TournamentDocument;
import com.example.spring_vfdwebsite.entities.TournamentDocumentId;
import com.example.spring_vfdwebsite.entities.User;
import com.example.spring_vfdwebsite.entities.enums.TournamentStatusEnum;
import com.example.spring_vfdwebsite.events.tournament.TournamentCreatedEvent;
import com.example.spring_vfdwebsite.events.tournament.TournamentDeletedEvent;
import com.example.spring_vfdwebsite.events.tournament.TournamentUpdatedEvent;
import com.example.spring_vfdwebsite.exceptions.EntityNotFoundException;
import com.example.spring_vfdwebsite.repositories.DocumentJpaRepository;
import com.example.spring_vfdwebsite.repositories.TournamentJpaRepository;
import com.example.spring_vfdwebsite.repositories.UserJpaRepository;
import com.example.spring_vfdwebsite.utils.SlugUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {

    private final TournamentJpaRepository tournamentRepository;
    private final UserJpaRepository userRepository;
    private final DocumentJpaRepository documentRepository;
    private final ApplicationEventPublisher eventPublisher;

    // ===================== Create =====================
    @Override
    @Transactional
    @CacheEvict(value = { "tournaments", "galleries", "registration-forms" }, allEntries = true)
    @LoggableAction(value = "CREATE", entity = "tournaments", description = "Create a new tournament")
    public TournamentResponseDto createTournament(TournamentCreateRequestDto dto) {

        // Lấy user hiện tại
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((org.springframework.security.core.userdetails.User) principal).getUsername();

        User currentUser = userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        String slug = generateUniqueSlug(dto.getName());

        // Tạo Tournament entity
        Tournament tournament = Tournament.builder()
                .name(dto.getName())
                .slug(slug)
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .location(dto.getLocation())
                .isVisibleOnHome(dto.getIsVisibleOnHome())
                .registrationOpen(dto.getRegistrationOpen())
                .bannerUrl(dto.getBannerUrl())
                .scheduleImages(dto.getScheduleImages())
                .createdBy(currentUser)
                .status(determineStatus(dto.getStartDate(), dto.getEndDate()))
                .build();

        tournamentRepository.save(tournament);

        // Gán documents thông qua pivot entity TournamentDocument
        if (dto.getRelatedDocumentIds() != null && !dto.getRelatedDocumentIds().isEmpty()) {
            List<Document> documents = documentRepository.findAllById(dto.getRelatedDocumentIds());

            Set<TournamentDocument> tournamentDocuments = documents.stream()
                    .map(doc -> TournamentDocument.builder()
                            .id(new TournamentDocumentId(tournament.getId(), doc.getId()))
                            .tournament(tournament)
                            .document(doc)
                            .build())
                    .collect(Collectors.toSet());
            tournament.setTournamentDocuments(tournamentDocuments);
            // Lưu lại để update liên kết pivot
            tournamentRepository.save(tournament);
        }

        eventPublisher.publishEvent(new TournamentCreatedEvent(tournament.getId(), tournament));

        return toDto(tournament);
    }

    // ===================== Update =====================
    @Override
    @Transactional
    @CachePut(value = "tournaments", key = "#p0")
    @CacheEvict(value = { "tournaments", "galleries", "registration-forms" }, allEntries = true)
    @LoggableAction(value = "UPDATE", entity = "tournaments", description = "Update an existing tournament")
    public TournamentResponseDto updateTournament(Integer tournamentId, TournamentUpdateRequestDto dto) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id: " + tournamentId));

        boolean nameChanged = dto.getName() != null && !dto.getName().equals(tournament.getName());

        if (dto.getName() != null) {
            tournament.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            tournament.setDescription(dto.getDescription());
        }
        if (dto.getStartDate() != null) {
            tournament.setStartDate(dto.getStartDate());
        }
        if (dto.getEndDate() != null) {
            tournament.setEndDate(dto.getEndDate());
        }
        if (dto.getLocation() != null) {
            tournament.setLocation(dto.getLocation());
        }
        if (dto.getIsVisibleOnHome() != null) {
            tournament.setIsVisibleOnHome(dto.getIsVisibleOnHome());
        }
        if (dto.getRegistrationOpen() != null) {
            tournament.setRegistrationOpen(dto.getRegistrationOpen());
        }
        if (dto.getBannerUrl() != null) {
            tournament.setBannerUrl(dto.getBannerUrl());
        }
        if (dto.getScheduleImages() != null) {
            tournament.setScheduleImages(dto.getScheduleImages());
        }
        if (dto.getRelatedDocumentIds() != null) {
            // delete existing relations
            tournament.getTournamentDocuments().clear();

            List<Document> newDocs = documentRepository.findAllById(dto.getRelatedDocumentIds());

            for (Document doc : newDocs) {
                TournamentDocument td = TournamentDocument.builder()
                        .id(new TournamentDocumentId(tournament.getId(), doc.getId()))
                        .tournament(tournament)
                        .document(doc)
                        .build();
                tournament.getTournamentDocuments().add(td);
            }
        }

        // --- Xử lý quan trọng ---
        // Nếu người dùng gửi status → dùng user
        if (dto.getStatus() != null) {
            tournament.setStatus(dto.getStatus());

        } else {
            // Không gửi → hệ thống auto
            tournament.setStatus(determineStatus(
                    tournament.getStartDate(),
                    tournament.getEndDate()));
        }

        boolean slugMissing = tournament.getSlug() == null || tournament.getSlug().isBlank();
        if (slugMissing || nameChanged) {
            tournament.setSlug(generateUniqueSlug(tournament.getName()));
        }

        Tournament updatedTournament = tournamentRepository.save(tournament);

        eventPublisher.publishEvent(new TournamentUpdatedEvent(updatedTournament.getId(), updatedTournament));

        return toDto(updatedTournament);
    }

    // ===================== Get By Id =====================
    @Override
    @Cacheable(value = "tournaments", key = "#root.args[0]")
    @Transactional(readOnly = true)
    public TournamentResponseDto getTournamentById(Integer tournamentId) {
        Tournament tournament = tournamentRepository.findByIdTournament(tournamentId)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id: " + tournamentId));
        return toDto(tournament);
    }

    // ===================== Get All =====================
    @Override
    @Transactional(readOnly = true)
    public List<TournamentResponseDto> getAllTournaments() {
        System.out.println("🔥 Fetching all tournaments from the database...");
        List<Tournament> tournaments = tournamentRepository.findAllTournament();
        return tournaments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ===================== Get All Without Match Schedules =====================
    @Override
    @Transactional(readOnly = true)
    public List<TournamentResponseDto> getAllTournamentsWithoutMatchSchedules() {
        System.out.println("🔥 Fetching all tournaments WITHOUT match schedules from the database...");
        List<Tournament> tournaments = tournamentRepository.findAllTournamentsWithoutMatchSchedules();
        return tournaments.stream()
                .map(this::toDtoWithoutMatchSchedules)
                .collect(Collectors.toList());
    }

    // ===================== Delete =====================
    @Override
    @Transactional
    @CacheEvict(value = { "tournaments", "galleries", "registration-forms" }, allEntries = true)
    @LoggableAction(value = "DELETE", entity = "tournaments", description = "Delete an existing tournament")
    public void deleteTournament(Integer tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id: " + tournamentId));
        tournamentRepository.delete(tournament);
        eventPublisher.publishEvent(new TournamentDeletedEvent(tournamentId));
    }

    // ===================== Get By Slug =====================
    @Override
    @Cacheable(value = "tournaments", key = "#root.args[0]")
    @Transactional(readOnly = true)
    public TournamentResponseDto getTournamentBySlug(String slug) {
        Tournament tournament = tournamentRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found with slug: " + slug));
        return toDto(tournament);
    }

    // ===================== Get By Id and Slug =====================
    @Override
    @Cacheable(value = "tournaments", key = "#root.args[0] + '-' + #root.args[1]")
    @Transactional(readOnly = true)
    public TournamentResponseDto getTournamentByIdSlug(Integer id, String slug) {
        Tournament tournament = tournamentRepository.findByIdAndSlug(id, slug)
                .orElseThrow(
                        () -> new EntityNotFoundException("Tournament not found with id " + id + " and slug " + slug));
        return toDto(tournament);
    }

    // ===================== Count Tournaments =====================
    @Override
    public long countTournaments() {
        return tournamentRepository.count();
    }

    // ===================== Mapping -> Dto =====================
    private TournamentResponseDto toDto(Tournament tournament) {
        TournamentResponseDto.CreatedByDto createdByDto = TournamentResponseDto.CreatedByDto.builder()
                .id(tournament.getCreatedBy().getId())
                .fullName(tournament.getCreatedBy().getFullName())
                .email(tournament.getCreatedBy().getEmail())
                .imageUrl(tournament.getCreatedBy().getImageUrl())
                .build();

        // ========== Map Related Documents ==========
        List<TournamentResponseDto.DocumentDto> documentDtos = tournament.getTournamentDocuments() == null
                ? List.of()
                : tournament.getTournamentDocuments().stream()
                        .map(td -> TournamentResponseDto.DocumentDto.builder()
                                .id(td.getDocument().getId())
                                .title(td.getDocument().getTitle())
                                .fileUrl(td.getDocument().getFileUrl())
                                .build())
                        .toList();

        // ========== Map Match Schedules ==========
        List<TournamentResponseDto.MatchScheduleDto> matchScheduleDtos = tournament.getMatchSchedules() == null
                ? List.of()
                : tournament.getMatchSchedules().stream()
                        .map(ms -> TournamentResponseDto.MatchScheduleDto.builder()
                                .id(ms.getId())
                                .round(ms.getRound().name())
                                .groupTable(ms.getGroupTable())
                                .matchDate(ms.getMatchDate())
                                .teamA(ms.getTeamA())
                                .teamB(ms.getTeamB())
                                .scoreA(ms.getScoreA())
                                .scoreB(ms.getScoreB())
                                .build())
                        .toList();

        return TournamentResponseDto.builder()
                .id(tournament.getId())
                .name(tournament.getName())
                .slug(tournament.getSlug())
                .description(tournament.getDescription())
                .startDate(tournament.getStartDate())
                .endDate(tournament.getEndDate())
                .location(tournament.getLocation())
                .status(tournament.getStatus())
                .isVisibleOnHome(tournament.getIsVisibleOnHome())
                .registrationOpen(tournament.getRegistrationOpen())
                .bannerUrl(tournament.getBannerUrl())
                // .scheduleImages(tournament.getScheduleImages())
                .scheduleImages(
                        tournament.getScheduleImages() != null
                                ? new ArrayList<>(tournament.getScheduleImages())
                                : List.of())
                .tournamentDocuments(documentDtos)
                .matchSchedules(matchScheduleDtos)
                .createdBy(createdByDto)
                .createdAt(tournament.getCreatedAt())
                .updatedAt(tournament.getUpdatedAt())
                .build();
    }

    // ===================== Mapping -> Dto without matchSchedules =====================
    private TournamentResponseDto toDtoWithoutMatchSchedules(Tournament tournament) {
        // ========== Map CreatedBy ==========
        TournamentResponseDto.CreatedByDto createdByDto = TournamentResponseDto.CreatedByDto.builder()
                .id(tournament.getCreatedBy().getId())
                .fullName(tournament.getCreatedBy().getFullName())
                .email(tournament.getCreatedBy().getEmail())
                .imageUrl(tournament.getCreatedBy().getImageUrl())
                .build();

        // ========== Map Related Documents ==========
        List<TournamentResponseDto.DocumentDto> documentDtos = tournament.getTournamentDocuments() == null
                ? List.of()
                : tournament.getTournamentDocuments().stream()
                        .map(td -> TournamentResponseDto.DocumentDto.builder()
                                .id(td.getDocument().getId())
                                .title(td.getDocument().getTitle())
                                .fileUrl(td.getDocument().getFileUrl())
                                .build())
                        .toList();

        // Build DTO mà KHÔNG map matchSchedules
        return TournamentResponseDto.builder()
                .id(tournament.getId())
                .name(tournament.getName())
                .slug(tournament.getSlug())
                .description(tournament.getDescription())
                .startDate(tournament.getStartDate())
                .endDate(tournament.getEndDate())
                .location(tournament.getLocation())
                .status(tournament.getStatus())
                .isVisibleOnHome(tournament.getIsVisibleOnHome())
                .registrationOpen(tournament.getRegistrationOpen())
                .bannerUrl(tournament.getBannerUrl())
                .scheduleImages(
                        tournament.getScheduleImages() != null
                                ? new ArrayList<>(tournament.getScheduleImages())
                                : List.of())
                .tournamentDocuments(documentDtos)
                .createdBy(createdByDto)
                .createdAt(tournament.getCreatedAt())
                .updatedAt(tournament.getUpdatedAt())
                .build();
    }

    private TournamentStatusEnum determineStatus(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        if (startDate != null && today.isBefore(startDate))
            return TournamentStatusEnum.UPCOMING;
        if (startDate != null && endDate != null
                && (today.isEqual(startDate) || (today.isAfter(startDate) && today.isBefore(endDate)))) {
            return TournamentStatusEnum.ONGOING;
        }
        if (endDate != null && !today.isBefore(endDate))
            return TournamentStatusEnum.ENDED;
        return TournamentStatusEnum.UPCOMING;
    }

    private String generateUniqueSlug(String title) {
        String baseSlug = SlugUtil.toSlug(title);
        String slug = baseSlug;
        int counter = 1;

        while (tournamentRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }
        return slug;
    }

}
