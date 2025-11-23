package com.example.spring_vfdwebsite.dtos.otpDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// OtpVerificationResponseDto.java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpVerificationResponseDto {
    private Boolean isValid;
    private String message;
    private Long remainingAttempts;
}
