package com.example.spring_vfdwebsite.dtos.registrationFormDTOs;

import com.example.spring_vfdwebsite.entities.enums.RegistrationStatusEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for updating an existing registration form")
public class RegistrationFormUpdateRequestDto {

    @Schema(description = "Unique identifier of the news to update", example = "1")
    private Integer id;

    @Schema(description = "Full name of the registrant", example = "Nguyễn Văn B")
    @Size(max = 100, message = "fullName must be less than 100 characters")
    private String fullName;

    @Schema(description = "Email of the registrant", example = "newemail@gmail.com")
    @Email(message = "email must be valid")
    private String email;

    @Schema(description = "Phone number", example = "0905123456")
    private String phoneNumber;

    @Schema(description = "Team name", example = "Đội Hòa Xuân")
    @Size(max = 100, message = "teamName must be less than 100 characters")
    private String teamName;

    @Schema(description = "Unit registering the team", example = "Sở Văn hóa & Thể thao Đà Nẵng")
    @Size(max = 255, message = "registrationUnit must be less than 255 characters")
    private String registrationUnit;

    @Schema(description = "Number of athletes", example = "14")
    @Min(value = 1, message = "numberAthletes must be at least 1")
    private Integer numberAthletes;

    @Schema(description = "Updated file URL", example = "https://res.cloudinary.com/.../updated.pdf")
    @Size(max = 500, message = "fileUrl must be less than 500 characters")
    private String fileUrl;

    @Schema(description = "Registration status", example = "accepted")
    private RegistrationStatusEnum status;
}