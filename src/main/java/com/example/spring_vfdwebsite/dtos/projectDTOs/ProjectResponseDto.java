package com.example.spring_vfdwebsite.dtos.projectDTOs;

import java.time.LocalDateTime;

import com.example.spring_vfdwebsite.entities.enums.ProjectCategoryEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response DTO for Project entity")
public class ProjectResponseDto {

    @Schema(description = "Project ID", example = "1")
    private Integer id;

    @Schema(description = "Project title", example = "Dự án phát triển bóng chuyền trẻ")
    private String title;

    @Schema(description = "Project overview", example = "Chương trình phát triển tài năng bóng chuyền cho các vận động viên trẻ...")
    private String overview;

    @Schema(description = "Duration of the project", example = "4 - 6 tháng")
    private String duration;

    @Schema(description = "Project location", example = "Đà Nẵng")
    private String location;

    @Schema(description = "Price or cost of the project", example = "Miễn phí")
    private String price;

    @Schema(description = "Image URL", example = "/project/volleyball-youth-training.jpg")
    private String imageUrl;

    @Schema(
            description = "Project category",
            example = "development",
            allowableValues = {"development", "infrastructure", "collaboration", "training", "community"}
    )
    private ProjectCategoryEnum category;

    @Schema(description = "Bank information associated with the project")
    private BankDto bank;

    @Schema(description = "Timestamp when the project record was created", example = "2025-01-10T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the project record was last updated", example = "2025-01-15T12:20:45")
    private LocalDateTime updatedAt;

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BankDto {
        private Integer id;
        private String fullName;
        private String bankName;
        private String accountNumber;
        private String branch;
        private String imageUrl;
    }
}
