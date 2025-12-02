package com.example.spring_vfdwebsite.controllers;

import com.example.spring_vfdwebsite.dtos.bankDTOs.*;
import com.example.spring_vfdwebsite.services.bank.BankService;
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
@RequestMapping("/api/banks")
@RequiredArgsConstructor
@Tag(name = "Bank Controller", description = "API endpoints for managing banks")
public class BankController {

    private final BankService bankService;

    // ------------------- Create -------------------
    @Operation(summary = "Create a new bank", description = "Create a new bank and return the created bank", responses = {
            @ApiResponse(responseCode = "201", description = "Bank created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BankResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<BankResponseDto> createBank(@Valid @RequestBody BankCreateRequestDto dto) {
        BankResponseDto createdBank = bankService.createBank(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBank);
    }

    // ------------------- Get All -------------------
    @Operation(summary = "Get all banks", description = "Retrieve a list of all banks", responses = {
            @ApiResponse(responseCode = "200", description = "Banks retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<BankResponseDto>> getAllBanks() {
        List<BankResponseDto> banks = bankService.getAllBanks();
        return ResponseEntity.ok(banks);
    }

    // ------------------- Get by ID -------------------
    @Operation(summary = "Get bank by ID", description = "Retrieve a bank by its ID", responses = {
            @ApiResponse(responseCode = "200", description = "Bank retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BankResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Bank not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BankResponseDto> getBankById(@PathVariable("id") Integer id) {
        BankResponseDto bankDto = bankService.getBankById(id);
        return ResponseEntity.ok(bankDto);
    }

    // ------------------- Update -------------------
    @Operation(summary = "Update an existing bank", description = "Update the fields of an existing bank and return the updated bank", responses = {
            @ApiResponse(responseCode = "200", description = "Bank updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BankResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Bank not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<BankResponseDto> updateBank(@PathVariable("id") Integer id,
            @Valid @RequestBody BankUpdateRequestDto dto) {
        BankResponseDto updatedBank = bankService.updateBank(id, dto);
        return ResponseEntity.ok(updatedBank);
    }

    // ------------------- Delete -------------------
    @Operation(summary = "Delete a bank", description = "Delete a bank by its ID", responses = {
            @ApiResponse(responseCode = "204", description = "Bank deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Bank not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBank(@PathVariable("id") Integer id) {
        bankService.deleteBank(id);
        return ResponseEntity.noContent().build();
    }
}