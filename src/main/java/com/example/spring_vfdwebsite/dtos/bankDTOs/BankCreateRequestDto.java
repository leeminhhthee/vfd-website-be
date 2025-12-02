package com.example.spring_vfdwebsite.dtos.bankDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Schema(description = "DTO for creating a new bank")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankCreateRequestDto {
    @Schema(description = "Full name of the board director", example = "Nguyen Van A")
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @Schema(description = "Bank name", example = "Vietcombank")
    @NotBlank(message = "Bank name is required")
    @Size(max = 100, message = "Bank name must not exceed 100 characters")
    private String bankName;

    @Schema(description = "Account number", example = "0123456789012345")
    @NotBlank(message = "Account number is required")
    @Size(max = 20, message = "Account number must not exceed 20 characters")
    private String accountNumber;

    @Schema(description = "Branch name", example = "Hanoi Branch")
    @NotBlank(message = "Branch name is required")
    @Size(max = 100, message = "Branch name must not exceed 100 characters")
    private String branch;

    @Schema(description = "Image URL or Base64 encoded string representing the bank’s logo",
            example = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...")
    private String imageUrl;
}