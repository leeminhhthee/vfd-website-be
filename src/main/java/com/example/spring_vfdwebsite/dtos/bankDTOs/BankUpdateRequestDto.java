package com.example.spring_vfdwebsite.dtos.bankDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Schema(description = "DTO for updating a bank")
@Getter
@Setter
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BankUpdateRequestDto {
    @Schema(description = "Unique identifier of the bank", example = "1")
    @NotNull(message = "Bank ID is required")
    private Integer id;

    @Schema(description = "Full name of the board director", example = "Nguyen Van A")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @Schema(description = "Bank name", example = "Vietcombank")
    @Size(max = 100, message = "Bank name must not exceed 100 characters")
    private String bankName;

    @Schema(description = "Account number", example = "0123456789012345")
    @Size(max = 20, message = "Account number must not exceed 20 characters")
    private String accountNumber;

    @Schema(description = "Branch name", example = "Hanoi Branch")
    @Size(max = 100, message = "Branch name must not exceed 100 characters")
    private String branch;

    @Schema(description = "Image URL or Base64 encoded string representing the bank’s logo",
            example = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...")
    private String imageUrl;
}
