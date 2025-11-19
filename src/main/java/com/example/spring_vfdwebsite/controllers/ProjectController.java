package com.example.spring_vfdwebsite.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.spring_vfdwebsite.dtos.projectDTOs.ProjectCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.projectDTOs.ProjectResponseDto;
import com.example.spring_vfdwebsite.dtos.projectDTOs.ProjectUpdateRequestDto;
import com.example.spring_vfdwebsite.services.project.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Project", description = "APIs for managing projects")
public class ProjectController {
    private final ProjectService projectService;

    // ===================== Get All =====================
    @Operation(summary = "Get all projects", description = "Retrieve a list of all projects", responses = {
            @ApiResponse(responseCode = "200", description = "List of projects", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getAllProjects() {
        List<ProjectResponseDto> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    // ===================== Get By Id =====================
    @Operation(summary = "Get project by ID", description = "Retrieve project details by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Project found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> getProjectById(
            @Parameter(description = "ID of the project", required = true) @PathVariable("id") Integer id) {
        ProjectResponseDto project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    // ===================== Create =====================
    @Operation(summary = "Create new project", description = "Create a new project with details", responses = {
            @ApiResponse(responseCode = "200", description = "Project created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(
            @RequestBody ProjectCreateRequestDto dto) {
        ProjectResponseDto createdProject = projectService.createProject(dto);
        return ResponseEntity.ok(createdProject);
    }

    // ===================== Update =====================
    @Operation(summary = "Update project", description = "Update project details", responses = {
            @ApiResponse(responseCode = "200", description = "Project updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> updateProject(
            @PathVariable("id") Integer id,
            @RequestBody ProjectUpdateRequestDto dto) {
        dto.setId(id);
        ProjectResponseDto updatedProject = projectService.updateProject(dto);
        return ResponseEntity.ok(updatedProject);
    }

    // ===================== Delete =====================
    @Operation(summary = "Delete project", description = "Delete a project by ID", responses = {
            @ApiResponse(responseCode = "204", description = "Project deleted"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "ID of the project", required = true) @PathVariable("id") Integer id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
