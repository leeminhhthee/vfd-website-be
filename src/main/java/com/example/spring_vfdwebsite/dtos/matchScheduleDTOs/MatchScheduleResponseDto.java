package com.example.spring_vfdwebsite.dtos.matchScheduleDTOs;

import java.time.LocalDateTime;

import com.example.spring_vfdwebsite.entities.enums.RoundEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response DTO for MatchSchedule entity")
public class MatchScheduleResponseDto {
    @Schema(description = "ID of the match schedule", example = "1")
    private Integer id;

    @Schema(description = "Round of the match", example = "group")
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

    @Schema(description = "Score of team B", example = "0")
    private Integer scoreB;

    @Schema(description = "Basic info of the tournament this match belongs to")
    private TournamentInfo tournament;

    @Schema(description = "Timestamp when the match schedule was created", example = "2025-05-01T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the match schedule was last updated", example = "2025-05-10T12:20:45")
    private LocalDateTime updatedAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Basic Tournament Info for embedding in MatchScheduleResponseDto")
    public static class TournamentInfo {
        @Schema(description = "ID of the tournament", example = "9")
        private Integer id;

        @Schema(description = "Name of the tournament", example = "Giải Bóng Chuyền Sinh Viên Toàn Quốc SV6 VietSport Cup năm 2025")
        private String name;
    }
}
