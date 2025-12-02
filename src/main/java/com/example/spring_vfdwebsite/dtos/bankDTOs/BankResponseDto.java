package com.example.spring_vfdwebsite.dtos.bankDTOs;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "DTO for Bank response")
@Getter
@Setter
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BankResponseDto {
    @Schema(description = "Unique identifier of the board director", example = "1")
    private Integer id;

    @Schema(description = "Full name of the board director", example = "Nguyen Van A")
    private String fullName;

    @Schema(description = "Bank name", example = "Vietcombank")
    private String bankName;

    @Schema(description = "Account number", example = "0123456789012345")
    private String accountNumber;

    @Schema(description = "Branch name", example = "Hanoi Branch")
    private String branch;

    @Schema(description = "Image URL or Base64 encoded string representing the bank’s logo",
            example = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...")
    private String imageUrl;

    @Schema(description = "Timestamp when the record was created", example = "2025-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the record was last updated", example = "2025-03-05T15:20:00")
    private LocalDateTime updatedAt;
}
