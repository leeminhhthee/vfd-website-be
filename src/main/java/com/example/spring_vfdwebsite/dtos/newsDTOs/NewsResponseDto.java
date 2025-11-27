package com.example.spring_vfdwebsite.dtos.newsDTOs;

import java.time.LocalDateTime;

import com.example.spring_vfdwebsite.entities.enums.NewsStatusEnum;
import com.example.spring_vfdwebsite.entities.enums.NewsTypeEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response DTO for News entity")
public class NewsResponseDto {

    @Schema(description = "News ID", example = "1")
    private Integer id;

    @Schema(description = "News title", example = "Giải bóng chuyền thành phố Đà Nẵng 2025")
    private String title;

    @Schema(description = "News type", example = "city", allowableValues = { "city", "international", "inside_vn", "other" })
    private NewsTypeEnum type;

    @Schema(description = "News content (HTML)", example = "<p>Nội dung tin tức...</p>")
    private String content;

    @Schema(description = "News status", example = "published", allowableValues = { "draft", "published" })
    private NewsStatusEnum status;

    @Schema(description = "Image URL", example = "https://res.cloudinary.com/.../image.jpg")
    private String imageUrl;

    @Schema(description = "Information about the author of the news")
    private AuthorByDto authorBy;

    @Schema(description = "Tags associated with the news", example = "sports, volleyball, city event")
    private String tags;

    @Schema(description = "Timestamp when the project record was created", example = "2025-01-10T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the project record was last updated", example = "2025-01-15T12:20:45")
    private LocalDateTime updatedAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AuthorByDto {
        private Integer id;
        private String fullName;
        private String email;
        private String imageUrl;
    }
}
