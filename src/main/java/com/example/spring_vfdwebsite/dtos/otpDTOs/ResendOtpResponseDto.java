package com.example.spring_vfdwebsite.dtos.otpDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// ResendOtpResponseDto.java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResendOtpResponseDto {
    private String message;
    private String email;
    private Long cooldownSeconds;
}
