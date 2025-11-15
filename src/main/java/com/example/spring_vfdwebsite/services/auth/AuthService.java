package com.example.spring_vfdwebsite.services.auth;

import com.example.spring_vfdwebsite.dtos.authDTOs.LoginRequestDto;
import com.example.spring_vfdwebsite.dtos.authDTOs.LoginResponseDto;

public interface AuthService {
    
    /**
     * Login a user with email and password.
     * @param email the email of the user
     * @param password the password of the user
     * @return LoginResponseDto of the logged-in user
     */
    LoginResponseDto loginUser(LoginRequestDto requestDto);

    LoginResponseDto refreshToken(String refreshToken);
}
