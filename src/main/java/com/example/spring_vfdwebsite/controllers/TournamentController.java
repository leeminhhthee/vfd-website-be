package com.example.spring_vfdwebsite.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.spring_vfdwebsite.dtos.tournamentDTOs.*;
import com.example.spring_vfdwebsite.services.tournament.TournamentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
@Tag(name = "Tournament Controller", description = "API endpoint for managing tournaments")
public class TournamentController {
        private final TournamentService tournamentService;

        // ===================== Create =====================
        @Operation(summary = "Create a new tournament", description = "Create a new tournament with the provided details", responses = {
                        @ApiResponse(responseCode = "201", description = "Tournament created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TournamentResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input data")
        })
        @PostMapping
        public ResponseEntity<TournamentResponseDto> createTournament(
                        @Validated @RequestBody TournamentCreateRequestDto dto) {
                TournamentResponseDto created = tournamentService.createTournament(dto);
                return ResponseEntity.ok(created);
        }

        // ===================== Update =====================
        @Operation(summary = "Update an existing tournament by ID", description = "Update the details of an existing tournament", responses = {
                        @ApiResponse(responseCode = "200", description = "Tournament updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TournamentResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input data"),
                        @ApiResponse(responseCode = "404", description = "Tournament not found")
        })
        @PatchMapping("/{tournamentId}")
        public ResponseEntity<TournamentResponseDto> updateTournament(
                        @PathVariable("tournamentId") Integer tournamentId,
                        @Validated @RequestBody TournamentUpdateRequestDto dto) {
                dto.setId(tournamentId);
                TournamentResponseDto updated = tournamentService.updateTournament(tournamentId, dto);
                return ResponseEntity.ok(updated);
        }

        // ==================== Delete =====================
        @Operation(summary = "Delete a tournament by ID", description = "Delete the tournament with the specified ID", responses = {
                        @ApiResponse(responseCode = "204", description = "Tournament deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "Tournament not found")
        })
        @DeleteMapping("/{tournamentId}")
        public ResponseEntity<Void> deleteTournament(
                        @PathVariable("tournamentId") Integer tournamentId) {
                tournamentService.deleteTournament(tournamentId);
                return ResponseEntity.noContent().build();
        }

        // ===================== Get By Id =====================
        @Operation(summary = "Get a tournament by ID", description = "Retrieve the details of a tournament by its ID", responses = {
                        @ApiResponse(responseCode = "200", description = "Tournament retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TournamentResponseDto.class))),
                        @ApiResponse(responseCode = "404", description = "Tournament not found")
        })
        @GetMapping("/{tournamentId}")
        public ResponseEntity<TournamentResponseDto> getTournamentById(
                        @Parameter(description = "ID of the tournament to retrieve") @PathVariable("tournamentId") Integer tournamentId) {
                TournamentResponseDto tournament = tournamentService.getTournamentById(tournamentId);
                return ResponseEntity.ok(tournament);
        }

        // ===================== Get All =====================
        @GetMapping
        @Operation(summary = "Get all tournaments", description = "Retrieve a list of all tournaments", responses = {
                        @ApiResponse(responseCode = "200", description = "List of tournaments", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TournamentResponseDto.class)))
        })
        public ResponseEntity<List<TournamentResponseDto>> getAllTournaments() {
                List<TournamentResponseDto> tournaments = tournamentService.getAllTournaments();
                return ResponseEntity.ok(tournaments);
        }

        // ===================== Get By Slug =====================
        @GetMapping("/slug/{slug}")
        @Operation(summary = "Get a tournament by slug", description = "Retrieve the details of a tournament by its slug", responses = {
                        @ApiResponse(responseCode = "200", description = "Tournament retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TournamentResponseDto.class))),
                        @ApiResponse(responseCode = "404", description = "Tournament not found")
        })
        public ResponseEntity<TournamentResponseDto> getTournamentBySlug(
                        @Parameter(description = "Slug of the tournament to retrieve") @PathVariable("slug") String slug) {
                TournamentResponseDto tournament = tournamentService.getTournamentBySlug(slug);
                return ResponseEntity.ok(tournament);
        }

        // ===================== Get By Id and Slug =====================
        @GetMapping("/{id}/{slug}")
        @Operation(summary = "Get a tournament by ID and slug", description = "Retrieve the details of a tournament by its ID and slug", responses = {
                        @ApiResponse(responseCode = "200", description = "Tournament retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TournamentResponseDto.class))),
                        @ApiResponse(responseCode = "404", description = "Tournament not found")
        })
        public ResponseEntity<TournamentResponseDto> getTournamentByIdSlug(
                        @Parameter(description = "ID of the tournament to retrieve") @PathVariable("id") Integer id,
                        @Parameter(description = "Slug of the tournament to retrieve") @PathVariable("slug") String slug) {
                TournamentResponseDto tournament = tournamentService.getTournamentByIdSlug(id, slug);
                return ResponseEntity.ok(tournament);
        }
}