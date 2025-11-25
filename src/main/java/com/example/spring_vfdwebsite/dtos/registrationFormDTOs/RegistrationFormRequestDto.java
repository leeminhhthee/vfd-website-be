package com.example.spring_vfdwebsite.dtos.registrationFormDTOs;

import com.example.spring_vfdwebsite.entities.enums.RegistrationStatusEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request DTO for change status of Registration Form")
public class RegistrationFormRequestDto {
    @Schema(description = "Registration status", example = "approved", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = { "pending", "approved", "rejected" })
    @NotNull(message = "status is required")
    private RegistrationStatusEnum status;

    @Schema(description = "Administrative note", example = "Approved with conditions")
    private String adminNote;
    
}
