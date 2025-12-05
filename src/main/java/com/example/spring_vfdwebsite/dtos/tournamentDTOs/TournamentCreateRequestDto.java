package com.example.spring_vfdwebsite.dtos.tournamentDTOs;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request DTO to create a Tournament")
public class TournamentCreateRequestDto {
    @NotNull
    @Size(max = 255)
    @Schema(description = "Name of the tournament", example = "Giải Bóng Chuyền SV6 2025")
    private String name;

    @Size(max = 255)
    @Schema(description = "Slug of the tournament", example = "giai-bong-chuyen-sv6-2025")
    private String slug;

    @Schema(description = "Description of the tournament", example = "Giải đấu quy mô quốc gia dành cho sinh viên...")
    private String description;

    @Schema(description = "Start date of the tournament", example = "2025-05-19")
    private LocalDate startDate;

    @Schema(description = "End date of the tournament", example = "2025-05-23")
    private LocalDate endDate;

    @Size(max = 255)
    @Schema(description = "Location of the tournament", example = "Nhà thi đấu Đại học Đà Nẵng")
    private String location;

    @Schema(description = "Whether the tournament is visible on the home page", example = "true")
    private Boolean isVisibleOnHome;

    @Schema(description = "Whether registration is open for this tournament", example = "false")
    private Boolean registrationOpen;

    @Size(max = 500)
    @Schema(description = "Banner URL for the tournament", example = "https://res.cloudinary.com/.../banner.jpg")
    private String bannerUrl;

    @Schema(description = "List of URLs for schedule images")
    private List<String> scheduleImages;

    @Schema(description = "IDs of related documents")
    private List<Integer> relatedDocumentIds;
}
