package com.example.spring_vfdwebsite.dtos.affectedObjectDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO for creating a new Affected Object")
public class AffectedObjectCreateRequestDto {

    @Schema(description = "Title or name of the affected object", example = "Main Stadium Renovation", required = true)
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @Schema(description = "Detailed description about the affected object", example = "Renovation of the main stadium including new seating, lighting, and field improvements.")
    private String description;

    @Schema(description = "Image URL representing the affected object", example = "https://example.com/images/affected-object-1.png")
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;
}
