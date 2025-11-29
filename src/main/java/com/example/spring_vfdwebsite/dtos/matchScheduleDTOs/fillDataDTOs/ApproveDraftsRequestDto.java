package com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.fillDataDTOs;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request DTO for approving match schedule drafts")
public class ApproveDraftsRequestDto {
    @NotNull
    @Schema(description = "ID of the match schedule draft to approve", example = "1")
    @NotEmpty(message = "Draft IDs cannot be empty")
    private List<Integer> id;
}
