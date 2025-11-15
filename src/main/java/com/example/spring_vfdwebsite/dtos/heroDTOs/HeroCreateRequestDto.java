package com.example.spring_vfdwebsite.dtos.heroDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for creating a Hero")
public class HeroCreateRequestDto {

    @Size(max = 100)
    @Schema(description = "Title of the hero", example = "Liên đoàn bóng chuyền Đà Nẵng")
    private String title;

    @Schema(description = "Subtitle or description of the hero", example = "Nơi phát triển tài năng, tổ chức các giải đấu chuyên nghiệp và nâng cao trình độ bóng chuyền tại Đà Nẵng.")
    private String subTitle;

    @Size(max = 500)
    @Schema(description = "URL of the hero image", example = "https://res.cloudinary.com/dikzmjuff/image/upload/v1761996319/hero1_wdzew9.jpg")
    private String imageUrl;

    @Size(max = 100)
    @Schema(description = "Text for the primary button", example = "Đăng ký thành viên")
    private String buttonText;

    @Size(max = 500)
    @Schema(description = "Link for the primary button", example = "/membership")
    private String buttonHref;

    @Size(max = 100)
    @Schema(description = "Text for the secondary button", example = "Giới thiệu")
    private String buttonText2;

    @Size(max = 500)
    @Schema(description = "Link for the secondary button", example = "/about")
    private String buttonHref2;
}
