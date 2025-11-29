package com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.fillDataDTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class SaveDraftsRequestDto {
    @NotNull(message = "Tournament ID is required")
    private Integer tournamentId;

    @NotNull(message = "Matches list cannot be null")
    private List<MatchAiDto> matches; // Danh sách data từ AI (hoặc admin đã sửa)
}