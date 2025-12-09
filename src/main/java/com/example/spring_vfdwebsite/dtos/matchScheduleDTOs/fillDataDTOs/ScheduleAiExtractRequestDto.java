package com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.fillDataDTOs;

import java.util.List;

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
    @Schema(description = "List of schedule image URLs", example = "[\"https://img1.jpg\", \"https://img2.jpg\"]")
    private List<String> imageUrls;

    @NotNull(message = "Tournament ID is required")
    @Schema(description = "ID of the tournament this schedule belongs to", example = "10")
    private Integer tournamentId;
}
