package com.example.spring_vfdwebsite.dtos.documentDTOs;

import com.example.spring_vfdwebsite.entities.enums.DocumentCategoryEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for creating a new Document")
public class DocumentCreateRequestDto {

    @Schema(description = "Title of the document", example = "Kế hoạch Giải Bóng chuyền Cup CLB TP Đà Nẵng 2025", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "title is required")
    @Size(max = 255, message = "title must be less than 255 characters")
    private String title;

    @Schema(description = "Category of the document: plan, charter, forms, regulations", example = "plan", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "category is required")
    private DocumentCategoryEnum category;

    @Schema(description = "Original file name stored on Cloudinary", example = "ke-hoach-2025.pdf")
    private String fileName;

    @Schema(description = "Cloudinary file URL", example = "https://res.cloudinary.com/.../file.pdf")
    @Size(max = 500, message = "fileUrl must be less than 500 characters")
    private String fileUrl;

    @Schema(description = "MIME type of the uploaded file", example = "application/pdf")
    private String fileType;

    @Schema(description = "File size in bytes", example = "2048000")
    private Long fileSize;
}
