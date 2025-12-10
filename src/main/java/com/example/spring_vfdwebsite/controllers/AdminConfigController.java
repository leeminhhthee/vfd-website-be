package com.example.spring_vfdwebsite.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.spring_vfdwebsite.services.systemConfig.SystemConfigService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/config")
@RequiredArgsConstructor
@Tag(name = "System Config Controller", description = "API endpoint for managing system configurations")
public class AdminConfigController {

    private final SystemConfigService configService;

    // ===================== Get Activity Log Status =====================
    @Operation(summary = "Get Activity Log Status", description = "Retrieve the current status of the activity log feature", responses = {
            @ApiResponse(responseCode = "200", description = "Activity log status retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/activity-log")
    public ResponseEntity<?> getActivityLogStatus() {
        boolean enabled = configService
                .getConfigValueByKey(SystemConfigService.ACTIVITY_LOG_KEY)
                .map(Boolean::parseBoolean)
                .orElse(true); // fallback default
        return ResponseEntity.ok(Map.of("enabled", enabled));
    }

    // ===================== Set Activity Log Status =====================
    @Operation(summary = "Set Activity Log Status", description = "Update the status of the activity log feature", responses = {
            @ApiResponse(responseCode = "200", description = "Activity log status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/activity-log")
    public ResponseEntity<?> setActivityLogStatus(@RequestBody Map<String, Object> body) {
        Object enabledObj = body.get("enabled");
        if (!(enabledObj instanceof Boolean enabled)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "'enabled' must be boolean"));
        }
        configService.setConfigValue(SystemConfigService.ACTIVITY_LOG_KEY, enabled.toString());
        return ResponseEntity.ok(Map.of("enabled", enabled));
    }
}
