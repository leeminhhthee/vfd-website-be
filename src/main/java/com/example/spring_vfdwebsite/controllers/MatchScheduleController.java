package com.example.spring_vfdwebsite.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.*;
import com.example.spring_vfdwebsite.services.matchSchedule.MatchScheduleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/match-schedules")
@RequiredArgsConstructor
@Tag(name = "Match Schedule Controller", description = "API endpoint for managing match schedules")
public class MatchScheduleController {
        private final MatchScheduleService matchScheduleService;

        // ===================== Create =====================
        @Operation(summary = "Create a new match schedule", description = "Create a new match schedule with the provided details", responses = {
                        @ApiResponse(responseCode = "201", description = "Match schedule created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MatchScheduleResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input data")
        })
        @PostMapping
        public ResponseEntity<MatchScheduleResponseDto> createMatchSchedule(
                        @Validated @RequestBody MatchScheduleCreateRequestDto dto) {
                MatchScheduleResponseDto created = matchScheduleService.createMatchSchedule(dto);
                return ResponseEntity.ok(created);
        }

        // ===================== Update =====================
        @Operation(summary = "Update an existing match schedule by ID", description = "Update the details of an existing match schedule", responses = {
                        @ApiResponse(responseCode = "200", description = "Match schedule updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MatchScheduleResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input data"),
                        @ApiResponse(responseCode = "404", description = "Match schedule not found")
        })
        @PatchMapping("/{id}")
        public ResponseEntity<MatchScheduleResponseDto> updateMatchSchedule(
                        @Parameter(description = "ID of the match schedule to update") @PathVariable("id") Integer id,
                        @Validated @RequestBody MatchScheduleUpdateRequestDto dto) {
                dto.setId(id);
                MatchScheduleResponseDto updated = matchScheduleService.updateMatchSchedule(id, dto);
                return ResponseEntity.ok(updated);
        }

        // ===================== Delete =====================
        @Operation(summary = "Delete a match schedule by ID", description = "Delete the match schedule with the specified ID", responses = {
                        @ApiResponse(responseCode = "204", description = "Match schedule deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "Match schedule not found")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteMatchSchedule(
                        @Parameter(description = "ID of the match schedule to delete") @PathVariable("id") Integer id) {
                matchScheduleService.deleteMatchSchedule(id);
                return ResponseEntity.noContent().build();
        }

        // ===================== Get All =====================
        @GetMapping
        @Operation(summary = "Get all match schedules", description = "Retrieve a list of all match schedules", responses = {
                        @ApiResponse(responseCode = "200", description = "List of match schedules", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MatchScheduleResponseDto.class)))
        })
        public ResponseEntity<List<MatchScheduleResponseDto>> getAllMatchSchedules() {
                List<MatchScheduleResponseDto> schedules = matchScheduleService.getAllMatchSchedules();
                return ResponseEntity.ok(schedules);
        }

        // ===================== Get by ID =====================
        @GetMapping("/{id}")
        @Operation(summary = "Get a match schedule by ID", description = "Retrieve the details of a match schedule by its ID", responses = {
                        @ApiResponse(responseCode = "200", description = "Match schedule found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MatchScheduleResponseDto.class))),
                        @ApiResponse(responseCode = "404", description = "Match schedule not found")
        })
        public ResponseEntity<MatchScheduleResponseDto> getMatchScheduleById(
                        @Parameter(description = "ID of the match schedule to retrieve") @PathVariable("id") Integer id) {
                MatchScheduleResponseDto schedule = matchScheduleService.getMatchScheduleById(id);
                return ResponseEntity.ok(schedule);
        }

        // ===================== Get by Tournament =====================
        @GetMapping("/tournament/{tournamentId}")
        @Operation(summary = "Get match schedules by tournament ID", description = "Retrieve a list of match schedules for a specific tournament", responses = {
                        @ApiResponse(responseCode = "200", description = "List of match schedules for the tournament", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MatchScheduleResponseDto.class))),
                        @ApiResponse(responseCode = "404", description = "Tournament not found")
        })
        public ResponseEntity<List<MatchScheduleResponseDto>> getMatchSchedulesByTournament(
                        @Parameter(description = "ID of the tournament") @PathVariable("tournamentId") Integer tournamentId) {
                List<MatchScheduleResponseDto> schedules = matchScheduleService
                                .getMatchSchedulesByTournament(tournamentId);
                return ResponseEntity.ok(schedules);
        }
}