package com.example.spring_vfdwebsite.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.fillDataDTOs.ApproveDraftsRequestDto;
import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.fillDataDTOs.MatchAiDto;
import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.fillDataDTOs.MatchScheduleDraftResponseDto;
import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.fillDataDTOs.SaveDraftsRequestDto;
import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.fillDataDTOs.ScheduleAiExtractRequestDto;
import com.example.spring_vfdwebsite.services.matchSchedule.MatchScheduleDraftService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/match-schedule-drafts")
@RequiredArgsConstructor
@Tag(name = "Match Schedule Draft Controller", description = "API endpoint for managing match schedule drafts")
public class MatchScheduleDraftController {

    private final MatchScheduleDraftService matchScheduleDraftService;

    // Get all
    @Operation(summary = "Get all match schedule drafts", description = "Retrieve a list of all match schedule drafts", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of match schedule drafts", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MatchScheduleDraftResponseDto.class))))
    })
    @GetMapping
    public ResponseEntity<List<MatchScheduleDraftResponseDto>> getAllMatchScheduleDrafts() {
        List<MatchScheduleDraftResponseDto> drafts = matchScheduleDraftService.findAll();
        return ResponseEntity.ok(drafts);
    }

    // Get by tournament
    @Operation(summary = "Get match schedule drafts by tournament ID", description = "Retrieve a list of match schedule drafts for a specific tournament", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of match schedule drafts for the tournament", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MatchScheduleDraftResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "Tournament not found")
    })
    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<List<MatchScheduleDraftResponseDto>> getMatchScheduleDraftsByTournament(
            @Parameter(description = "ID of the tournament") @PathVariable("tournamentId") Integer tournamentId) {
        List<MatchScheduleDraftResponseDto> drafts = matchScheduleDraftService.findByTournament(tournamentId);
        return ResponseEntity.ok(drafts);
    }

    // Get by ID
    @Operation(summary = "Get match schedule draft by ID", description = "Retrieve a match schedule draft by its ID", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved match schedule draft", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MatchScheduleDraftResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "Match schedule draft not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MatchScheduleDraftResponseDto> getMatchScheduleDraftById(
            @Parameter(description = "ID of the match schedule draft") @PathVariable("id") Integer id) {
        MatchScheduleDraftResponseDto draft = matchScheduleDraftService.findById(id);
        if (draft != null) {
            return ResponseEntity.ok(draft);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Extract matches API: Upload image → AI extract → return list of draft matches
    @Operation(summary = "Extract match schedule drafts from image", description = "Extract match schedule drafts from an image URL using AI", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully extracted match schedule drafts", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MatchAiDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid image URL")
    })
    @PostMapping("/ai/extract")
    public ResponseEntity<List<MatchAiDto>> extractMatchScheduleDrafts(
            @Valid @RequestBody ScheduleAiExtractRequestDto requestDto) {
        List<MatchAiDto> matches = matchScheduleDraftService.extractMatches(requestDto.getImageUrl());
        return ResponseEntity.ok(matches);
    }

    // Approve drafts
    @Operation(summary = "Approve match schedule drafts", description = "Approve one or more match schedule drafts", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully approved match schedule drafts"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "One or more match schedule drafts not found")
    })
    @PostMapping("/approve")
    public ResponseEntity<Void> approveMatchScheduleDrafts(
            @Valid @RequestBody ApproveDraftsRequestDto dto) {
        matchScheduleDraftService.approveDrafts(dto);
        return ResponseEntity.ok().build();
    }

    // Save drafts
    @Operation(summary = "Save match schedule drafts", description = "Save one or more match schedule drafts", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully saved match schedule drafts", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MatchScheduleDraftResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Tournament not found")
    })
    @PostMapping("/save")
    public ResponseEntity<List<MatchScheduleDraftResponseDto>> saveMatchScheduleDrafts(
            @Valid @RequestBody SaveDraftsRequestDto dto) {
        List<MatchScheduleDraftResponseDto> savedDrafts = matchScheduleDraftService.saveDrafts(dto);
        return ResponseEntity.ok(savedDrafts);
    }
}