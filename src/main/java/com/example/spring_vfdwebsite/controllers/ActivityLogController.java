package com.example.spring_vfdwebsite.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.spring_vfdwebsite.dtos.activityLogDTOs.*;
import com.example.spring_vfdwebsite.entities.User;
import com.example.spring_vfdwebsite.exceptions.EntityNotFoundException;
import com.example.spring_vfdwebsite.repositories.UserJpaRepository;
import com.example.spring_vfdwebsite.services.activityLog.ActivityLogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/activity-logs")
@RequiredArgsConstructor
@Tag(name = "Activity Log Controller", description = "API endpoint for managing activity logs")
public class ActivityLogController {

        private final ActivityLogService activityLogService;
        private final UserJpaRepository userRepository;

        // ===================== Get All Activity Logs =====================
        @Operation(summary = "Get all activity logs", description = "Retrieve a list of all activity logs with user information", responses = {
                        @ApiResponse(responseCode = "200", description = "List of activity logs retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ActivityLogResponseDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @GetMapping
        public ResponseEntity<List<ActivityLogResponseDto>> getAllActivityLogs() {
                List<ActivityLogResponseDto> activityLogs = activityLogService.getAllActivityLogs();
                return ResponseEntity.ok(activityLogs);
        }

        // ==================== Get Paginated Activity Logs =====================
        @Operation(summary = "Get paginated activity logs", description = "Retrieve a paginated list of activity logs", responses = {
                        @ApiResponse(responseCode = "200", description = "Paginated activity logs retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedAcivityLogResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @GetMapping("/paginated")
        public ResponseEntity<PaginatedAcivityLogResponseDto> getPaginatedActivityLogs(
                        @Parameter(description = "Page number (1-based)", example = "1") @RequestParam(defaultValue = "1") int pageNumber,
                        @Parameter(description = "Number of items per page", example = "10") @RequestParam(defaultValue = "10") int pageSize) {
                int actualPageNumber = Math.max(1, pageNumber);
                PaginatedAcivityLogResponseDto paginatedActivityLogs = activityLogService.getPaginatedActivityLogs(
                                actualPageNumber - 1,
                                pageSize);
                return ResponseEntity.ok(paginatedActivityLogs);
        }

        // ==================== Create Activity Log =====================
        @Operation(summary = "Create a new activity log", description = "Create a new activity log with the provided details", responses = {
                        @ApiResponse(responseCode = "201", description = "Activity log created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ActivityLogResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input data"),
                        @ApiResponse(responseCode = "401", description = "User not authenticated")
        })
        @PostMapping
        public ResponseEntity<ActivityLogResponseDto> createActivityLog(
                        @Valid @RequestBody ActivityLogCreateRequestDto dto) {
                User currentUser = getCurrentAuthenticatedUser();
                ActivityLogResponseDto createdActivityLog = activityLogService.createActivityLogResponseDto(currentUser,
                                dto);
                return ResponseEntity.status(201).body(createdActivityLog);
        }

        // ==================== Delete 1 Activity Log =====================
        @Operation(summary = "Delete an activity log by ID", description = "Delete the activity log with the specified ID", responses = {
                        @ApiResponse(responseCode = "204", description = "Activity log deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "Activity log not found")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteActivityLog(
                        @Parameter(description = "ID of the activity log to delete") @PathVariable("id") Integer id) {
                activityLogService.deleteActivityLog(List.of(id));
                return ResponseEntity.noContent().build();
        }

        // ==================== Delete List Activity Log =====================
        @Operation(summary = "Delete multiple activity logs by IDs", description = "Delete multiple activity logs with the specified IDs", responses = {
                        @ApiResponse(responseCode = "204", description = "Activity logs deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "One or more activity logs not found")
        })
        @DeleteMapping
        public ResponseEntity<Void> deleteActivityLogs(
                        @Parameter(description = "IDs of the activity logs to delete") @RequestBody List<Integer> ids) {
                activityLogService.deleteActivityLog(ids);
                return ResponseEntity.noContent().build();
        }

        // ==================== Get Action Types =====================
        @Operation(summary = "Get all action types", description = "Retrieve a list of all possible action types for activity logs", responses = {
                        @ApiResponse(responseCode = "200", description = "List of action types retrieved successfully", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @GetMapping("/action-types")
        public ResponseEntity<List<String>> getActionTypes() {
                List<String> actionTypes = activityLogService.getActionTypes();
                return ResponseEntity.ok(actionTypes);
        }

        // ==================== Utility method =====================
        private User getCurrentAuthenticatedUser() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null || !authentication.isAuthenticated()
                                || "anonymousUser".equals(authentication.getPrincipal())) {
                        throw new SecurityException("User is not authenticated");
                }
                Object principal = authentication.getPrincipal();
                if (!(principal instanceof UserDetails userDetails)) {
                        throw new SecurityException("Invalid principal type");
                }
                return userRepository.findByEmailIgnoreCase(userDetails.getUsername())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "User not found: " + userDetails.getUsername()));
        }

        // ==================== 10 Latest Activity Logs =====================
        @Operation(summary = "Get 10 latest activity logs", description = "Retrieve the 10 most recent activity logs", responses = {
                        @ApiResponse(responseCode = "200", description = "List of latest activity logs retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ActivityLogResponseDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @GetMapping("/latest")
        public ResponseEntity<List<ActivityLogResponseDto>> getLatestActivityLogs() {
                List<ActivityLogResponseDto> latestLogs = activityLogService.getLatestActivityLogs();
                return ResponseEntity.ok(latestLogs);
        }
}
