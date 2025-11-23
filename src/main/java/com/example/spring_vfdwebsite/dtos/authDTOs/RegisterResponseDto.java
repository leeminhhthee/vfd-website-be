package com.example.spring_vfdwebsite.dtos.authDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseDto {
    private Integer id;
    private String email;
    private String fullName;
    private String message;
    private String accessToken;
}
