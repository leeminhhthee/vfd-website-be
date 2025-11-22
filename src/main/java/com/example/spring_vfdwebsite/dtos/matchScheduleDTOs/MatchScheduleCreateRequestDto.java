package com.example.spring_vfdwebsite.dtos.matchScheduleDTOs;

import java.time.LocalDateTime;

import com.example.spring_vfdwebsite.entities.enums.RoundEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request DTO for creating a new Match Schedule")
public class MatchScheduleCreateRequestDto {
    @NotNull
    @Schema(description = "Round of the match", example = "group")
    private RoundEnum round;

    @Schema(description = "Table/Group name if applicable", example = "Bảng A")
    private String groupTable;

    @NotNull
    @Schema(description = "Match date and time", example = "2025-05-19T14:00:00")
    private LocalDateTime matchDate;

    @NotNull
    @Schema(description = "Name of team A", example = "Đại học Đà Nẵng")
    private String teamA;

    @NotNull
    @Schema(description = "Name of team B", example = "Trường ĐH Tôn Đức Thắng")
    private String teamB;

    @Schema(description = "Score of team A (optional, usually set after match)", example = "0")
    private Integer scoreA;

    @Schema(description = "Score of team B (optional, usually set after match)", example = "0")
    private Integer scoreB;

    @NotNull
    @Schema(description = "ID of the tournament this match belongs to", example = "9")
    private Integer tournamentId;
}
