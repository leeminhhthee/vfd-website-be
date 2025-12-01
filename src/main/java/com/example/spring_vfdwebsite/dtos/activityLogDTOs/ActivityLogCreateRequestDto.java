package com.example.spring_vfdwebsite.dtos.activityLogDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for creating a new Activity Log")
public class ActivityLogCreateRequestDto {
    
    @Schema(description = "Type of action performed", example = "CREATE", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "actionType is required")
    @Size(max = 50, message = "actionType must be less than 50 characters")
    private String actionType;

    @Schema(description = "Target table of the action", example = "documents", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "targetTable is required")
    @Size(max = 100, message = "targetTable must be less than 100 characters")
    private String targetTable;

    @Schema(description = "Identifier of the target record", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "targetId is required")
    private Integer targetId;

    @Schema(description = "Detailed description of the activity", example = "User John Doe created a new document.", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "description is required")
    @Size(max = 1000, message = "description must be less than 1000 characters")
    private String description;

    @Schema(description = "Identifier of the user who performed the action", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "userId is required")
    private Integer userId;
}
