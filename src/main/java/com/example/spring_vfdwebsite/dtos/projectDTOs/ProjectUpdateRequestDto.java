package com.example.spring_vfdwebsite.dtos.projectDTOs;

import java.util.List;

import com.example.spring_vfdwebsite.entities.enums.ProjectCategoryEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for updating an existing Project")
public class ProjectUpdateRequestDto {
    @Schema(description = "ID of the project to update", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    @Schema(description = "Updated title of the project", example = "Dự án phát triển bóng chuyền trẻ nâng cao")
    @Size(max = 255)
    private String title;

    @Schema(description = "Updated project slug", example = "du-an-phat-trien-bong-chuyen-tre-nang-cao")
    private String slug;

    @Schema(description = "Updated project overview", example = "Chương trình nâng cao kỹ năng bóng chuyền cho VĐV trẻ...")
    private String overview;

    @Schema(description = "Updated project duration", example = "6 - 9 tháng")
    @Size(max = 100)
    private String duration;

    @Schema(description = "Updated project location", example = "Hồ Chí Minh")
    @Size(max = 255)
    private String location;

    @Schema(description = "Updated price/cost", example = "$200,000")
    @Size(max = 100)
    private String price;

    @Schema(description = "Updated project image", example = "/project/updated-image.jpg")
    @Size(max = 500)
    private String imageUrl;

    @Schema(description = "Updated project category", example = "infrastructure", allowableValues = { "development",
            "infrastructure", "collaboration", "training", "community" })
    private ProjectCategoryEnum category;

    @Schema(description = "Updated goals of the project", example = "[\"Updated Goal 1\", \"Updated Goal 2\"]")
    private List<String> goals;

    @Schema(description = "Updated bank information associated with the project")
    private Integer bankId;
}