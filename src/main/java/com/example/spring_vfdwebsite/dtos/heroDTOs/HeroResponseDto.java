package com.example.spring_vfdwebsite.dtos.heroDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for Hero response")
public class HeroResponseDto {

    @Schema(description = "Unique identifier of the hero", example = "1")
    private Integer id;

    @Schema(description = "Title of the hero", example = "Liên đoàn bóng chuyền Đà Nẵng")
    private String title;

    @Schema(description = "Subtitle or description of the hero", example = "Nơi phát triển tài năng, tổ chức các giải đấu chuyên nghiệp và nâng cao trình độ bóng chuyền tại Đà Nẵng.")
    private String subTitle;

    @Schema(description = "URL of the hero image", example = "https://res.cloudinary.com/dikzmjuff/image/upload/v1761996319/hero1_wdzew9.jpg")
    private String imageUrl;

    @Schema(description = "Text for the primary button", example = "Đăng ký thành viên")
    private String buttonText;

    @Schema(description = "Link for the primary button", example = "/membership")
    private String buttonHref;

    @Schema(description = "Text for the secondary button", example = "Giới thiệu")
    private String buttonText2;

    @Schema(description = "Link for the secondary button", example = "/about")
    private String buttonHref2;

    @Schema(description = "Timestamp when this record was created", example = "2025-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when this record was last updated", example = "2025-03-05T15:20:00")
    private LocalDateTime updatedAt;
}
