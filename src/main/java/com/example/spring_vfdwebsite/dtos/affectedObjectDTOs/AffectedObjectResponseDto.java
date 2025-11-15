package com.example.spring_vfdwebsite.dtos.affectedObjectDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "DTO for Affected Object response")
@Getter
@Setter
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AffectedObjectResponseDto {
        @Schema(description = "Unique identifier of the affected object", example = "1")
        private Integer id;

        @Schema(description = "Title or name of the affected object", example = "Main Stadium Renovation")
        private String title;

        @Schema(description = "Detailed description about the affected object", example = "Renovation of the main stadium including new seating, lighting, and field improvements.")
        private String description;

        @Schema(description = "Image URL or Base64 string representing the affected object's image", example = "https://example.com/images/affected-object-1.png")
        private String imageUrl;

        @Schema(description = "Timestamp when the affected object record was created", example = "2025-01-15T10:30:00")
        private LocalDateTime createdAt;

        @Schema(description = "Timestamp when the affected object record was last updated", example = "2025-03-05T15:20:00")
        private LocalDateTime updatedAt;
}
