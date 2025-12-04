package com.example.spring_vfdwebsite.dtos.tournamentDTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.spring_vfdwebsite.entities.enums.TournamentStatusEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response DTO for Tournament entity")
public class TournamentResponseDto {
    @Schema(description = "ID of the tournament", example = "1")
    private Integer id;

    @Schema(description = "Name of the tournament", example = "Giải Bóng Chuyền SV6 2025")
    private String name;

    @Schema(description = "Description of the tournament", example = "Giải đấu quy mô quốc gia dành cho sinh viên...")
    private String description;

    @Schema(description = "Start date of the tournament", example = "2025-05-19")
    private LocalDate startDate;

    @Schema(description = "End date of the tournament", example = "2025-05-23")
    private LocalDate endDate;

    @Schema(description = "Location of the tournament", example = "Nhà thi đấu Đại học Đà Nẵng")
    private String location;

    @Schema(description = "Status of the tournament", example = "ongoing")
    private TournamentStatusEnum status;

    @Schema(description = "Whether the tournament is visible on the home page", example = "true")
    private Boolean isVisibleOnHome;

    @Schema(description = "Whether registration is open for this tournament", example = "false")
    private Boolean registrationOpen;

    @Schema(description = "Banner URL for the tournament", example = "https://res.cloudinary.com/.../banner.jpg")
    private String bannerUrl;

    @Schema(description = "Schedule images for the tournament")
    private List<String> scheduleImages;

    @Schema(description = "Documents related to the tournament", example = "List of related documents")
    private List<DocumentDto> tournamentDocuments ;

    @Schema(description = "Match schedules of the tournament")
    private List<MatchScheduleDto> matchSchedules;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreatedByDto {
        private Integer id;
        private String fullName;
        private String email;
        private String imageUrl;
    }

    @Schema(description = "Information about the user who uploaded the document")
    private CreatedByDto createdBy;

    @Schema(description = "Timestamp when the tournament was created", example = "2025-05-19T08:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the tournament was last updated", example = "2025-05-20T12:00:00")
    private LocalDateTime updatedAt;   
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DocumentDto {
        private Integer id;
        private String title;
        private String fileUrl;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MatchScheduleDto {
        private Integer id;
        private String round;
        private String groupTable;
        private LocalDateTime matchDate;
        private String teamA;
        private String teamB;
        private Integer scoreA;
        private Integer scoreB;
    }
}
