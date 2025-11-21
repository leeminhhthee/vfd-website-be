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

    @Schema(description = "Unit registering the team", example = "Liên đoàn Bóng chuyền Đà Nẵng")
    private String registrationUnit;

    @Schema(description = "Number of athletes", example = "12")
    private Integer numberAthletes;

    @Schema(description = "URL to uploaded file", example = "https://res.cloudinary.com/.../file.pdf")
    private String fileUrl;

    @Schema(description = "Registration status", example = "pending", allowableValues = { "pending", "approved", "rejected" })
    private RegistrationStatusEnum status;

    @Schema(description = "Timestamp when the project record was created", example = "2025-01-10T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the project record was last updated", example = "2025-01-15T12:20:45")
    private LocalDateTime updatedAt;
}
