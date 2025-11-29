package com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.fillDataDTOs;

import java.time.LocalDateTime;

import com.example.spring_vfdwebsite.entities.enums.RoundEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request DTO for updating an existing Match Schedule Draft")
public class MatchScheduleDraftUpdateRequestDto {
    @NotNull
    @Schema(description = "ID of the match to update", example = "1")
    private Integer id;

    @Schema(description = "Round of the match", example = "semi-final")
    private RoundEnum round;

    @Schema(description = "Table/Group name if applicable", example = "Bảng A")
    private String groupTable;

    @Schema(description = "Match date and time", example = "2025-05-19T14:00:00")
    private LocalDateTime matchDate;

    @Schema(description = "Name of team A", example = "Đại học Đà Nẵng")
    @NotBlank(message = "Team A cannot be empty")
    private String teamA;

    @Schema(description = "Name of team B", example = "Trường ĐH Tôn Đức Thắng")
    @NotBlank(message = "Team B cannot be empty")
    private String teamB;
}
