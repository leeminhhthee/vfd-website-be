package com.example.spring_vfdwebsite.dtos.partnerDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for Partner response")
public class PartnerResponseDto {

    @Schema(description = "Unique identifier of the partner", example = "1")
    private Integer id;

    @Schema(description = "Name of the partner", example = "FPT Software")
    private String name;

    @Schema(description = "Email of the partner", example = "contact@fpt.com")
    private String email;

    @Schema(description = "URL of the partner logo or image", example = "https://example.com/logo.png")
    private String imageUrl;

    @Schema(description = "The year or date since the partner has been involved", example = "2020-05-10")
    private LocalDate since;

    @Schema(description = "Timestamp when this record was created", example = "2025-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when this record was last updated", example = "2025-03-05T15:20:00")
    private LocalDateTime updatedAt;
}
