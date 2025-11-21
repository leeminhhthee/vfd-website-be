package com.example.spring_vfdwebsite.dtos.registrationFormDTOs;

import com.example.spring_vfdwebsite.entities.enums.RegistrationStatusEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request DTO for creating a new Registration Form")
public class RegistrationFormCreateRequestDto {

    @Schema(description = "Full name of the registrant", example = "Nguyễn Văn A", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "fullName is required")
    @Size(max = 100, message = "fullName must be less than 100 characters")
    private String fullName;

    @Schema(description = "Email of the registrant", example = "example@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "email is required")
    @Email(message = "email must be valid")
    private String email;

    @Schema(description = "Phone number", example = "0987654321")
    private String phoneNumber;

    @Schema(description = "Team name", example = "Đội Ngũ Hành Sơn", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "teamName is required")
    @Size(max = 100, message = "teamName must be less than 100 characters")
    private String teamName;

    @Schema(description = "Unit registering the team", example = "Liên đoàn Bóng chuyền Đà Nẵng", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "registrationUnit is required")
    @Size(max = 255, message = "registrationUnit must be less than 255 characters")
    private String registrationUnit;

    @Schema(description = "Number of athletes", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "numberAthletes is required")
    @Min(value = 1, message = "numberAthletes must be at least 1")
    private Integer numberAthletes;

    @Schema(description = "File URL (Cloudinary URL)", example = "https://res.cloudinary.com/.../file.pdf", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "fileUrl is required")
    @Size(max = 500, message = "fileUrl must be less than 500 characters")
    private String fileUrl;

    @Schema(description = "Registration status (defaults to pending if not provided)", example = "pending", allowableValues = { "pending", "approved", "rejected" })
    private RegistrationStatusEnum status;
}
