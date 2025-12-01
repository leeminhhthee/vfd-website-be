package com.example.spring_vfdwebsite.dtos.activityLogDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for Activity Log response")
public class ActivityLogResponseDto {

    @Schema(description = "Unique identifier of the activity log", example = "1")
    private Integer id;

    @Schema(description = "Information about the user who performed the action")
    private UserDto user;

    @Schema(description = "Type of action performed", example = "CREATE")
    private String actionType;

    @Schema(description = "Target table of the action", example = "documents")
    private String targetTable;

    @Schema(description = "Identifier of the target record", example = "5")
    private Integer targetId;

    @Schema(description = "Detailed description of the activity", example = "User John Doe created a new document.")
    private String description;

    @Schema(description = "Timestamp when the activity was created", example = "2025-08-20T10:30:00")
    private LocalDateTime createdAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserDto {
        @Schema(description = "Unique identifier of the user", example = "2")
        private Integer id;

        @Schema(description = "Full name of the user", example = "John Doe")
        private String fullName;

        @Schema(description = "Email address of the user", example = "john.doe@example.com")
        private String email;
    }
}
