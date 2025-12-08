package com.example.spring_vfdwebsite.dtos.registrationFormDTOs;

import java.time.LocalDateTime;
import com.example.spring_vfdwebsite.entities.enums.RegistrationStatusEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response DTO for Registration Form entity")
public class RegistrationFormResponseDto {

    @Schema(description = "ID of the registration form", example = "1")
    private Integer id;

    @Schema(description = "Full name of the registrant", example = "Nguyễn Văn A")
    private String fullName;

    @Schema(description = "Email of the registrant", example = "example@gmail.com")
    private String email;

    @Schema(description = "Phone number", example = "0987654321")
    private String phoneNumber;

    @Schema(description = "Team name", example = "Đội Thanh Xuân")
    private String teamName;

    @Schema(description = "Leader's name", example = "Lê Thị C")
    private String leader;

    @Schema(description = "Unit registering the team", example = "Liên đoàn Bóng chuyền Đà Nẵng")
    private String registrationUnit;

    @Schema(description = "Coach's name", example = "Trần Văn B")
    private String coach;

    @Schema(description = "Number of athletes", example = "12")
    private Integer numberAthletes;

    @Schema(description = "URL to uploaded file", example = "https://res.cloudinary.com/.../file.pdf")
    private String fileUrl;

    @Schema(description = "Registration status", example = "pending", allowableValues = { "pending", "approved", "rejected" })
    private RegistrationStatusEnum status;

    @Schema(description = "Tournament associated with the registration form")
    private TournamentDto tournament;

    @Schema(description = "Timestamp when the registration record was created", example = "2025-01-10T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the registration record was last updated", example = "2025-01-15T12:20:45")
    private LocalDateTime updatedAt;

    @Schema(description = "Administrative note", example = "Approved with conditions")
    private String adminNote;

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TournamentDto {
        private Integer id;
        private String name;
    }
}
