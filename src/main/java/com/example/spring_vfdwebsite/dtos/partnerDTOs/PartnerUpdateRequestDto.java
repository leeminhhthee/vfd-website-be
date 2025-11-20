package com.example.spring_vfdwebsite.dtos.partnerDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for updating an existing Partner")
public class PartnerUpdateRequestDto {

    @Schema(description = "Unique identifier of the partner to update", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Partner ID is required")
    private Integer id;

    @Schema(description = "Updated name of the partner", example = "FPT Software")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Schema(description = "Updated email of the partner", example = "contact@fpt.com")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "Updated image URL of the partner", example = "https://example.com/logo.png")
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;

    @Schema(description = "Updated start date of the partnership", example = "2020-05-10")
    private LocalDate since;
}
