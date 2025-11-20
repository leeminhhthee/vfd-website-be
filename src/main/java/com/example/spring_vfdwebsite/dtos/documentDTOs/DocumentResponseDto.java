package com.example.spring_vfdwebsite.dtos.documentDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

import com.example.spring_vfdwebsite.entities.enums.DocumentCategoryEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for Document response")
public class DocumentResponseDto {

    @Schema(description = "Unique identifier of the document", example = "1")
    private Integer id;

    @Schema(description = "Title of the document", example = "Kế hoạch Giải Bóng chuyền Cup CLB TP Đà Nẵng 2025")
    private String title;

    @Schema(description = "Category of the document", example = "plan")
    private DocumentCategoryEnum category;

    @Schema(description = "File name stored on Cloudinary", example = "document_001.pdf")
    private String fileName;

    @Schema(description = "File URL on Cloudinary", example = "https://res.cloudinary.com/.../file.pdf")
    private String fileUrl;

    @Schema(description = "MIME file type", example = "application/pdf")
    private String fileType;

    @Schema(description = "Formatted file size", example = "2.5 MB")
    private String fileSize;

    @Schema(description = "Record creation timestamp", example = "2025-08-20T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Record update timestamp", example = "2025-08-22T14:12:00")
    private LocalDateTime updatedAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UploadedByDto {
        private Integer id;
        private String fullName;
        private String email;
        private String imageUrl;
    }

    @Schema(description = "Information about the user who uploaded the document")
    private UploadedByDto uploadedBy;
}
