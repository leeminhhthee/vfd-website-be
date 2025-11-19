package com.example.spring_vfdwebsite.dtos.boardDirectorDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Schema(description = "DTO for updating an existing board director")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDirectorUpdateRequestDto {

    @Schema(description = "Unique identifier of the board director to update", example = "1")
    @NotNull(message = "ID is required")
    private Integer id;

    @Schema(description = "Full name of the board director", example = "Nguyen Van A")
    @Size(max = 50, message = "Full name cannot exceed 50 characters")
    private String fullName;

    @Schema(description = "Email address of the board director", example = "nguyenvana@example.com")
    @Email(message = "Invalid email format")
    @Size(max = 50, message = "Email cannot exceed 50 characters")
    private String email;

    @Schema(description = "Phone number of the board director (10 digits)", example = "0912345678")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @Schema(description = "Role or position of the board director", example = "Chairman")
    @Size(max = 255, message = "Role cannot exceed 255 characters")
    private String role;

    @Schema(description = "Term duration or period of the board director", example = "2023 - 2026")
    @Size(max = 255, message = "Term cannot exceed 255 characters")
    private String term;

    @Schema(description = "Short biography or background information of the board director",
            example = "Nguyen Van A has over 20 years of experience in sports management and leadership.")
    private String bio;

    @Schema(description = "Image URL or Base64 encoded string representing the director’s photo",
            example = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...")
    private String imageUrl;
}
