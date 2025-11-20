package com.example.spring_vfdwebsite.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.spring_vfdwebsite.dtos.galleryDTOs.GalleryCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.galleryDTOs.GalleryResponseDto;
import com.example.spring_vfdwebsite.dtos.galleryDTOs.GalleryUpdateRequestDto;
import com.example.spring_vfdwebsite.services.gallery.GalleryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/galleries")
@RequiredArgsConstructor
@Tag(name = "Gallery Controller", description = "API endpoints for managing galleries")
public class GalleryController {
    private final GalleryService galleryService;

    // ===================== Get All Galleries =====================
    @Operation(summary = "Get all galleries", description = "Retrieve a list of all galleries", responses = {
            @ApiResponse(responseCode = "200", description = "List of galleries", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GalleryResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<GalleryResponseDto>> getAllGalleries() {
        List<GalleryResponseDto> galleries = galleryService.getAllGalleries();
        return ResponseEntity.ok(galleries);
    }

    // ===================== Get Gallery By ID =====================
    @Operation(summary = "Get gallery by ID", description = "Retrieve gallery details by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Gallery found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GalleryResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Gallery not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GalleryResponseDto> getGalleryById(
            @Parameter(description = "ID of the gallery", required = true) @PathVariable("id") Integer id) {
        GalleryResponseDto gallery = galleryService.getGalleryById(id);
        return ResponseEntity.ok(gallery);
    }

    // ===================== Create Gallery =====================
    @Operation(summary = "Create new gallery", description = "Create a new gallery with details", responses = {
            @ApiResponse(responseCode = "200", description = "Gallery created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GalleryResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<GalleryResponseDto> createGallery(@RequestBody GalleryCreateRequestDto dto) {
        GalleryResponseDto createdGallery = galleryService.createGallery(dto);
        return ResponseEntity.ok(createdGallery);
    }

    // ===================== Update Gallery =====================
    @Operation(summary = "Update gallery", description = "Update gallery details", responses = {
            @ApiResponse(responseCode = "200", description = "Gallery updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GalleryResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Gallery not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<GalleryResponseDto> updateGallery(
            @Parameter(description = "ID of the gallery", required = true) @PathVariable("id") Integer id,
            @RequestBody GalleryUpdateRequestDto dto) {
        dto.setId(id);
        GalleryResponseDto updatedGallery = galleryService.updateGallery(id, dto);
        return ResponseEntity.ok(updatedGallery);
    }

    // ===================== Delete Gallery =====================
    @Operation(summary = "Delete gallery", description = "Delete a gallery by ID", responses = {
            @ApiResponse(responseCode = "204", description = "Gallery deleted"),
            @ApiResponse(responseCode = "404", description = "Gallery not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGallery(
            @Parameter(description = "ID of the gallery", required = true) @PathVariable("id") Integer id) {
        galleryService.deleteGallery(id);
        return ResponseEntity.noContent().build();
    }
}