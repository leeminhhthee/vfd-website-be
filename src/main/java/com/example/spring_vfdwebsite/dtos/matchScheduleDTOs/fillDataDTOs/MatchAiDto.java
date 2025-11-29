package com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.fillDataDTOs;

import java.time.LocalDateTime;

// import com.example.spring_vfdwebsite.entities.enums.RoundEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for Match AI Data")
public class MatchAiDto {

    @Schema(description = "ID of the match", example = "1")
    private Integer id;

    @Schema(description = "Round of the match", example = "group")
    // private RoundEnum round;
    private String round;

    @Schema(description = "Table/Group name if applicable", example = "Bảng A")
    private String groupTable;

    @Schema(description = "Match date and time", example = "2025-05-19T14:00:00")
    private LocalDateTime matchDate;

    @Schema(description = "Name of team A", example = "Đại học Đà Nẵng")
    private String teamA;

    @Schema(description = "Name of team B", example = "Trường ĐH Tôn Đức Thắng")
    private String teamB;
}
