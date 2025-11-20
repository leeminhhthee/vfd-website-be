package com.example.spring_vfdwebsite.controllers;

import com.example.spring_vfdwebsite.dtos.affectedObjectDTOs.*;
import com.example.spring_vfdwebsite.services.affectedObject.AffectedObjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/affected-objects")
@RequiredArgsConstructor
@Tag(name = "Affected Object Controller", description = "API endpoints for managing affected objects")
public class AffectedObjectController {

    private final AffectedObjectService service;

    // ------------------- Create -------------------
    @Operation(
            summary = "Create a new affected object",
            description = "Create a new affected object and return the created object",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Object created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AffectedObjectResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<AffectedObjectResponseDto> create(
            @Valid @RequestBody AffectedObjectCreateRequestDto dto) {
        AffectedObjectResponseDto created = service.createAffectedObject(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ------------------- Update -------------------
    @Operation(
            summary = "Update an existing affected object",
            description = "Update the fields of an existing affected object and return the updated object",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Object updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AffectedObjectResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Object not found", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<AffectedObjectResponseDto> update(
            @PathVariable("id") Integer id,
            @Valid @RequestBody AffectedObjectUpdateRequestDto dto) {
        AffectedObjectResponseDto updated = service.updateAffectedObject(id, dto);
        return ResponseEntity.ok(updated);
    }

    // ------------------- Delete -------------------
    @Operation(
            summary = "Delete an affected object",
            description = "Delete an affected object by its ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Object deleted successfully", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Object not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        service.deleteAffectedObject(id);
        return ResponseEntity.noContent().build();
    }

    // ------------------- Get by ID -------------------
    @Operation(
            summary = "Get an affected object by ID",
            description = "Retrieve a specific affected object by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Object retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AffectedObjectResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Object not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AffectedObjectResponseDto> getById(@PathVariable("id") Integer id) {
        AffectedObjectResponseDto dto = service.getAffectedObjectById(id);
        return ResponseEntity.ok(dto);
    }

    // ------------------- Get All -------------------
    @Operation(
            summary = "Get all affected objects",
            description = "Retrieve a list of all affected objects",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Objects retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AffectedObjectResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<List<AffectedObjectResponseDto>> getAll() {
        List<AffectedObjectResponseDto> list = service.getAllAffectedObjects();
        return ResponseEntity.ok(list);
    }
}

