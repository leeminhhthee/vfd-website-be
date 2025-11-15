package com.example.spring_vfdwebsite.dtos.affectedObjectDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO for updating an existing Affected Object")
public class AffectedObjectUpdateRequestDto {

    @Schema(description = "Unique identifier of the affected object to update", example = "1", required = true)
    private Integer id;

    @Schema(description = "Title or name of the affected object", example = "Main Stadium Renovation")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @Schema(description = "Detailed description about the affected object", example = "Updated description with new renovation details.")
    private String description;

    @Schema(description = "Image URL representing the affected object", example = "https://example.com/images/affected-object-1.png")
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;
}
