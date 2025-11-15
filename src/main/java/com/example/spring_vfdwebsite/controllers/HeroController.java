package com.example.spring_vfdwebsite.controllers;

import com.example.spring_vfdwebsite.dtos.heroDTOs.HeroCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.heroDTOs.HeroResponseDto;
import com.example.spring_vfdwebsite.dtos.heroDTOs.HeroUpdateRequestDto;
import com.example.spring_vfdwebsite.services.hero.HeroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/heroes")
@Tag(name = "Hero", description = "APIs for managing hero banners")
public class HeroController {

    private final HeroService heroService;

    // ===================== GET ALL =====================
    @Operation(
            summary = "Get all heroes",
            description = "Returns a list of all hero banners.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List fetched successfully",
                            content = @Content(schema = @Schema(implementation = HeroResponseDto.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<HeroResponseDto>> getAllHeroes() {
        return ResponseEntity.ok(heroService.getAllHeroes());
    }

    // ===================== GET ONE =====================
    @Operation(
            summary = "Get hero by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Hero found"),
                    @ApiResponse(responseCode = "404", description = "Hero not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<HeroResponseDto> getHeroById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(heroService.getHeroById(id));
    }

    // ===================== CREATE =====================
    @Operation(
            summary = "Create new hero",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Hero created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PostMapping
    public ResponseEntity<HeroResponseDto> createHero(
            @Valid @RequestBody HeroCreateRequestDto dto
    ) {
        HeroResponseDto response = heroService.createHero(dto);
        return ResponseEntity.status(201).body(response);
    }

    // ===================== UPDATE =====================
    @Operation(
            summary = "Update existing hero",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Hero updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Hero not found")
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<HeroResponseDto> updateHero(
            @PathVariable("id") Integer id,
            @Valid @RequestBody HeroUpdateRequestDto dto
    ) {
        dto.setId(id); // assign id
        return ResponseEntity.ok(heroService.updateHero(dto));
    }

    // ===================== DELETE =====================
    @Operation(
            summary = "Delete hero",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Hero deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Hero not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHero(@PathVariable("id") Integer id) {
        heroService.deleteHero(id);
        return ResponseEntity.noContent().build();
    }

}
