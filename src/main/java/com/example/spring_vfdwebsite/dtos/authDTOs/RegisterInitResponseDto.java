package com.example.spring_vfdwebsite.dtos.authDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// RegisterInitResponseDto.java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterInitResponseDto {
    private String message;
    private String email;
}
