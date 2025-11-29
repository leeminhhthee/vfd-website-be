package com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.fillDataDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request DTO for AI-based extraction of match schedule from an image")
public class ScheduleAiExtractRequestDto {
    @NotNull(message = "Image URL is required")
    @Schema(description = "URL of the schedule image", example = "http://example.com/image.jpg")
    private String imageUrl;

    @NotNull(message = "Tournament ID is required")
    @Schema(description = "ID of the tournament this schedule belongs to", example = "10")
    private Integer tournamentId;
}
