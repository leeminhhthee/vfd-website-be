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
@Schema(description = "DTO for creating a new Partner")
public class PartnerCreateRequestDto {

    @Schema(description = "Name of the partner", example = "FPT Software", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Schema(description = "Email of the partner", example = "contact@fpt.com")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "URL of the partner's image or logo", example = "https://example.com/logo.png")
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;

    @Schema(description = "Date since the partner has been involved", example = "2020-05-10")
    private LocalDate since;
}
