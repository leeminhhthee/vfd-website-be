package com.example.spring_vfdwebsite.dtos.otpDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ResendOtpRequestDto.java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResendOtpRequestDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
}

