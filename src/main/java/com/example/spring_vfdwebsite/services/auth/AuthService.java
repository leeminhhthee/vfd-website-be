package com.example.spring_vfdwebsite.services.auth;

import com.example.spring_vfdwebsite.dtos.authDTOs.LoginRequestDto;
import com.example.spring_vfdwebsite.dtos.authDTOs.LoginResponseDto;
import com.example.spring_vfdwebsite.dtos.authDTOs.RegisterInitRequestDto;
import com.example.spring_vfdwebsite.dtos.authDTOs.RegisterInitResponseDto;
import com.example.spring_vfdwebsite.dtos.authDTOs.RegisterResponseDto;
import com.example.spring_vfdwebsite.dtos.otpDTOs.ConfirmRegistrationDto;
import com.example.spring_vfdwebsite.dtos.otpDTOs.ResendOtpRequestDto;
import com.example.spring_vfdwebsite.dtos.otpDTOs.ResendOtpResponseDto;

public interface AuthService {
    
    /**
     * Login a user with email and password.
     * @param email the email of the user
     * @param password the password of the user
     * @return LoginResponseDto of the logged-in user
     */
    LoginResponseDto loginUser(LoginRequestDto requestDto);

    void logoutUser(String refreshToken);

    LoginResponseDto refreshToken(String refreshToken);

    RegisterInitResponseDto registerInit(RegisterInitRequestDto requestDto);

    RegisterResponseDto registerConfirm(ConfirmRegistrationDto requestDto);

    ResendOtpResponseDto resendOtp(ResendOtpRequestDto requestDto);
}
