package com.example.spring_vfdwebsite.dtos.newsDTOs;

import com.example.spring_vfdwebsite.entities.enums.NewsStatusEnum;
import com.example.spring_vfdwebsite.entities.enums.NewsTypeEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for updating an existing News")
public class NewsUpdateRequestDto {

    @Schema(description = "Unique identifier of the news to update", example = "1", required = true)
    private Integer id;

    @Size(max = 255, message = "Title must be at most 255 characters")
    @Schema(description = "News title", example = "Giải bóng chuyền Thành phố 2025")
    private String title;

    @Schema(description = "News type", example = "city", allowableValues = { "city", "international", "insite_vn", "other" })
    private NewsTypeEnum type;

    @Schema(description = "News content (HTML)", example = "<p>Nội dung tin tức...</p>")
    private String content;

    @Schema(description = "Status", example = "published", allowableValues = { "draft", "published" })
    private NewsStatusEnum status;

    @Size(max = 500, message = "Image URL must be at most 500 characters")
    @Schema(description = "Image URL", example = "https://res.cloudinary.com/...image.jpg")
    private String imageUrl;
}
