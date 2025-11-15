package com.example.spring_vfdwebsite.controllers;

import com.example.spring_vfdwebsite.dtos.boardDirectorDTOs.BoardDirectorResponseDto;
import com.example.spring_vfdwebsite.dtos.boardDirectorDTOs.BoardDirectorCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.boardDirectorDTOs.BoardDirectorUpdateRequestDto;
import com.example.spring_vfdwebsite.services.boardDirector.BoardDirectorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board-directors")
@Tag(name = "Board Director Management", description = "APIs for managing board directors")
//@SecurityRequirement(name = "bearerAuth")
public class BoardDirectorController {

    private final BoardDirectorService boardDirectorService;

    public BoardDirectorController(BoardDirectorService boardDirectorService) {
        this.boardDirectorService = boardDirectorService;
    }

    @Operation(summary = "Get all board directors")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all board directors")
    })
    @GetMapping
    public List<BoardDirectorResponseDto> getAllBoardDirectors() {
        return boardDirectorService.getAllBoardDirectors();
    }

    @Operation(summary = "Get board director by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved board director"),
            @ApiResponse(responseCode = "404", description = "Board director not found")
    })
    @GetMapping("/{id}")
    public BoardDirectorResponseDto getBoardDirectorById(@PathVariable Integer id) {
        return boardDirectorService.getBoardDirectorById(id);
    }

    @Operation(summary = "Create a new board director")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Board director created successfully"),
            @ApiResponse(responseCode = "409", description = "Board director with same email already exists")
    })
    @PostMapping
    public ResponseEntity<BoardDirectorResponseDto> createBoardDirector(
            @Valid @RequestBody BoardDirectorCreateRequestDto dto) {
        BoardDirectorResponseDto created = boardDirectorService.createBoardDirector(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update an existing board director")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Board director updated successfully"),
            @ApiResponse(responseCode = "404", description = "Board director not found")
    })
    @PatchMapping("/{id}")
    public BoardDirectorResponseDto updateBoardDirector(
            @PathVariable Integer id,
            @Valid @RequestBody BoardDirectorUpdateRequestDto dto) {
        dto.setId(id); // đảm bảo DTO có ID
        return boardDirectorService.updateBoardDirector(dto);
    }

    @Operation(summary = "Delete a board director")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Board director deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Board director not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoardDirector(@PathVariable Integer id) {
        boardDirectorService.deleteBoardDirector(id);
        return ResponseEntity.noContent().build();
    }
}
