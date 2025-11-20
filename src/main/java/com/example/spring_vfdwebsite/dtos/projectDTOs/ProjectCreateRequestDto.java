package com.example.spring_vfdwebsite.dtos.projectDTOs;

import com.example.spring_vfdwebsite.entities.enums.ProjectCategoryEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO for creating a new Project")
public class ProjectCreateRequestDto {
    @Schema(description = "Title of the project", example = "Dự án phát triển bóng chuyền trẻ", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Title is required")
    @Size(max = 255)
    private String title;

    @Schema(description = "Overview of the project", example = "Chương trình phát triển tài năng bóng chuyền cho các vận động viên trẻ...")
    private String overview;

    @Schema(description = "Duration of the project", example = "4 - 6 tháng")
    @Size(max = 100)
    private String duration;

    @Schema(description = "Project location", example = "Đà Nẵng")
    @Size(max = 255)
    private String location;

    @Schema(description = "Cost or price of the project", example = "Miễn phí")
    @Size(max = 100)
    private String price;

    @Schema(description = "Image URL of the project", example = "/project/volleyball-youth-training.jpg")
    @Size(max = 500)
    private String imageUrl;

    @Schema(description = "Category of the project", example = "development", allowableValues = { "development",
            "infrastructure", "collaboration", "training", "community" }, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Category is required")
    private ProjectCategoryEnum category;
}
