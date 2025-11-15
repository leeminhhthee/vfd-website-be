package com.example.spring_vfdwebsite.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.spring_vfdwebsite.dtos.partnerDTOs.PartnerCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.partnerDTOs.PartnerResponseDto;
import com.example.spring_vfdwebsite.dtos.partnerDTOs.PartnerUpdateRequestDto;
import com.example.spring_vfdwebsite.services.partner.PartnerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/partners")
@RequiredArgsConstructor
@Tag(name = "Partners", description = "APIs for managing partners")
public class PartnerController {

    private final PartnerService partnerService;
    
    // ===== Create Partner =====
    @Operation(
            summary = "Create a new partner",
            description = "Add a new partner and return its details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Partner created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PartnerResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<PartnerResponseDto> createPartner(@RequestBody PartnerCreateRequestDto dto) {
        PartnerResponseDto created = partnerService.createPartner(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ===== Update Partner =====
    @Operation(
            summary = "Update an existing partner",
            description = "Update a partner by ID and return updated details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Partner updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PartnerResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Partner not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<PartnerResponseDto> updatePartner(
            @PathVariable("id") Integer id,
            @RequestBody PartnerUpdateRequestDto dto) {
        dto.setId(id);
        PartnerResponseDto updated = partnerService.updatePartner(dto);
        return ResponseEntity.ok(updated);
    }

    // ===== Delete Partner =====
    @Operation(
            summary = "Delete a partner by ID",
            description = "Remove a partner by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Partner deleted successfully", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Partner not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartner(@PathVariable("id") Integer id) {
        partnerService.deletePartner(id);
        return ResponseEntity.noContent().build();
    }

    // ===== Get Partner by ID =====
    @Operation(
            summary = "Get partner by ID",
            description = "Retrieve partner details by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Partner found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PartnerResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Partner not found", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PartnerResponseDto> getPartnerById(@PathVariable("id") Integer id) {
        PartnerResponseDto partner = partnerService.getPartnerById(id);
        return ResponseEntity.ok(partner);
    }

    // ===== Get all Partners =====
    @Operation(
            summary = "Get all partners",
            description = "Retrieve list of all partners",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of partners",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PartnerResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<List<PartnerResponseDto>> getAllPartners() {
        List<PartnerResponseDto> partners = partnerService.getAllPartners();
        return ResponseEntity.ok(partners);
    }
}
