package com.example.spring_vfdwebsite.dtos.boardDirectorDTOs;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "DTO for Board Director response")
@Getter
@Setter
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BoardDirectorResponseDto {
    @Schema(description = "Unique identifier of the board director", example = "1")
    private Integer id;

    @Schema(description = "Full name of the board director", example = "Nguyen Van A")
    private String fullName;

    @Schema(description = "Email address of the board director", example = "nguyenvana@example.com")
    private String email;

    @Schema(description = "Phone number of the board director", example = "0912345678")
    private String phoneNumber;

    @Schema(description = "Role or position of the board director", example = "Chairman")
    private String role;

    @Schema(description = "Term duration or period of the board director", example = "2023 - 2026")
    private String term;

    @Schema(description = "Short biography or background information",
            example = "Nguyen Van A has over 20 years of experience in sports management and leadership.")
    private String bio;

    @Schema(description = "Image URL or Base64 encoded string representing the director’s photo",
            example = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...")
    private String image;

    @Schema(description = "Timestamp when the record was created", example = "2025-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the record was last updated", example = "2025-03-05T15:20:00")
    private LocalDateTime updatedAt;
}
