package com.example.spring_vfdwebsite.dtos.galleryDTOs;

import java.util.List;

import com.example.spring_vfdwebsite.entities.enums.GalleryCategoryEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request DTO for creating a new Gallery")
public class GalleryCreateRequestDto {
    @NotBlank(message = "Title must not be empty")
    @Size(max = 255, message = "Title must be at most 255 characters")
    @Schema(description = "Gallery title", example = "Giải bóng chuyền Thành phố 2025")
    private String title;

    @NotBlank(message = "Slug must not be empty")
    @Size(max = 255, message = "Slug must be at most 255 characters")
    @Schema(description = "Gallery slug", example = "giai-bong-chuyen-thanh-pho-2025")
    private String slug;

    @NotNull(message = "Category must not be null")
    @Schema(description = "Gallery category", example = "other", allowableValues = { "tournament", "team", "event", "other" })
    private GalleryCategoryEnum category;

    @NotNull(message = "Images must not be null")
    @Schema(description = "List of image URLs uploaded from frontend", example = "[\"https://res.cloudinary.com/.../img1.jpg\", \"https://res.cloudinary.com/.../img2.jpg\"]")
    private List<String> imageUrl;

    @Schema(description = "ID of the tournament this gallery belongs to", example = "1")
    private Integer tournament;
}
