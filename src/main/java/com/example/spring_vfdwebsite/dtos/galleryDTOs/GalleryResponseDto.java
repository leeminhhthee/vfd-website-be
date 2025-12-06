package com.example.spring_vfdwebsite.dtos.galleryDTOs;

import java.time.LocalDateTime;
import java.util.List;

import com.example.spring_vfdwebsite.entities.enums.GalleryCategoryEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response DTO for Gallery entity")
public class GalleryResponseDto {

    @Schema(description = "Gallery ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    @Schema(description = "Gallery title", example = "Nhận quyết định thành lập CLB bóng chuyền cộng đồng")
    private String title;

    @Schema(description = "Gallery slug", example = "nhan-quyet-dinh-thanh-lap-clb-bong-chuyen-cong-dong")
    private String slug;

    @Schema(description = "Gallery category", example = "other", allowableValues = { "tournament", "team", "event", "other" })
    private GalleryCategoryEnum category;

    @Schema(description = "List of image URLs", example = "[\"https://res.cloudinary.com/.../img1.jpg\", \"https://res.cloudinary.com/.../img2.jpg\"]")
    private List<String> imageUrl;

    @Schema(description = "Timestamp when the project record was created", example = "2025-01-10T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the project record was last updated", example = "2025-01-15T12:20:45")
    private LocalDateTime updatedAt;

    @Schema(description = "Basic info of the tournament this gallery belongs to")
    private TournamentDto tournament;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TournamentDto {
        @Schema(description = "ID of the tournament", example = "1")
        private Integer id;

        @Schema(description = "Name of the tournament", example = "Giải Bóng Chuyền SV6 2025")
        private String name;
    }
}
