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
@Schema(description = "Request DTO for updating an existing Match Schedule")
public class MatchScheduleUpdateRequestDto {

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
    private String teamA;

    @Schema(description = "Name of team B", example = "Trường ĐH Tôn Đức Thắng")
    private String teamB;

    @Schema(description = "Score of team A", example = "2")
    private Integer scoreA;

    @Schema(description = "Score of team B", example = "1")
    private Integer scoreB;
}