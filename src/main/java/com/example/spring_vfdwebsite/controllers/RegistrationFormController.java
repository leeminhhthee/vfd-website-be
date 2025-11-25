package com.example.spring_vfdwebsite.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.spring_vfdwebsite.dtos.registrationFormDTOs.*;
import com.example.spring_vfdwebsite.services.registrationForm.RegistrationFormService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/registration-forms")
@RequiredArgsConstructor
@Tag(name = "Registration Form Controller", description = "API endpoint for managing registration forms")
public class RegistrationFormController {
        private final RegistrationFormService registrationFormService;

        // ===================== Create =====================
        @Operation(summary = "Create a new registration form", description = "Create a new registration form with the provided details", responses = {
                        @ApiResponse(responseCode = "201", description = "Registration form created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationFormResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input data")
        })
        @PostMapping
        public ResponseEntity<RegistrationFormResponseDto> createRegistrationForm(
                        @Validated @RequestBody RegistrationFormCreateRequestDto dto) {
                RegistrationFormResponseDto created = registrationFormService.createRegistrationForm(dto);
                return ResponseEntity.ok(created);
        }

        // ===================== Update =====================
        @Operation(summary = "Update an existing registration form by ID", description = "Update the details of an existing registration form", responses = {
                        @ApiResponse(responseCode = "200", description = "Registration form updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationFormResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input data"),
                        @ApiResponse(responseCode = "404", description = "Registration form not found")
        })
        @PatchMapping("/{id}")
        public ResponseEntity<RegistrationFormResponseDto> updateRegistrationForm(
                        @Parameter(description = "ID of the registration form to update", required = true) @PathVariable("id") Integer id,
                        @Validated @RequestBody RegistrationFormUpdateRequestDto dto) {
                dto.setId(id);
                RegistrationFormResponseDto updated = registrationFormService.updateRegistrationForm(id, dto);
                return ResponseEntity.ok(updated);
        }

        // ===================== Change Status =====================
        @Operation(summary = "Change the status of a registration form", description = "Change the status of the registration form with the specified ID", responses = {
                        @ApiResponse(responseCode = "200", description = "Registration form status updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationFormResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid status value"),
                        @ApiResponse(responseCode = "404", description = "Registration form not found")
        })
        @PatchMapping("/{id}/status")
        public ResponseEntity<RegistrationFormResponseDto> changeRegistrationFormStatus(
                        @Parameter(description = "ID of the registration form to update status", required = true) @PathVariable("id") Integer id,
                        @Validated @RequestBody RegistrationFormRequestDto dto) {
                RegistrationFormResponseDto updated = registrationFormService.changeRegistrationFormStatus(id, dto);
                return ResponseEntity.ok(updated);
        }

        // ===================== Delete =====================
        @Operation(summary = "Delete a registration form by ID", description = "Delete the registration form with the specified ID", responses = {
                        @ApiResponse(responseCode = "204", description = "Registration form deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "Registration form not found")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteRegistrationForm(
                        @Parameter(description = "ID of the registration form to delete", required = true) @PathVariable("id") Integer id) {
                registrationFormService.deleteRegistrationForm(id);
                return ResponseEntity.noContent().build();
        }

        // ===================== Get by ID =====================
        @Operation(summary = "Get a registration form by ID", description = "Retrieve the details of a registration form by its ID", responses = {
                        @ApiResponse(responseCode = "200", description = "Registration form found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationFormResponseDto.class))),
                        @ApiResponse(responseCode = "404", description = "Registration form not found")
        })
        @GetMapping("/{id}")
        public ResponseEntity<RegistrationFormResponseDto> getRegistrationFormById(
                        @Parameter(description = "ID of the registration form to retrieve", required = true) @PathVariable("id") Integer id) {
                RegistrationFormResponseDto dto = registrationFormService.getRegistrationFormById(id);
                return ResponseEntity.ok(dto);
        }

        // ===================== Get all =====================
        @Operation(summary = "Get all registration forms", description = "Retrieve a list of all registration forms", responses = {
                        @ApiResponse(responseCode = "200", description = "List of registration forms", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationFormResponseDto.class)))
        })
        @GetMapping
        public ResponseEntity<List<RegistrationFormResponseDto>> getAllRegistrationForms() {
                List<RegistrationFormResponseDto> list = registrationFormService.getAllRegistrationForms();
                return ResponseEntity.ok(list);
        }
}
