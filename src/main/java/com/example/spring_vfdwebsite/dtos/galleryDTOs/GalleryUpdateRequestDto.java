package com.example.spring_vfdwebsite.dtos.galleryDTOs;

import java.util.List;

import com.example.spring_vfdwebsite.entities.Gallery.GalleryCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request DTO for updating an existing Gallery")
public class GalleryUpdateRequestDto {

    @Schema(description = "ID of the gallery to update", example = "1", required = true)
    private Integer id;

    @Size(max = 255, message = "Title must be at most 255 characters")
    @Schema(description = "Gallery title", example = "Giải bóng chuyền Thành phố 2025")
    private String title;

    @Schema(description = "Gallery category", example = "team", allowableValues = { "insite", "team", "other" })
    private GalleryCategory category;

    @Schema(description = "List of image URLs uploaded from frontend", example = "[\"https://res.cloudinary.com/.../img1.jpg\", \"https://res.cloudinary.com/.../img2.jpg\"]")
    private List<String> imageUrl;
}
