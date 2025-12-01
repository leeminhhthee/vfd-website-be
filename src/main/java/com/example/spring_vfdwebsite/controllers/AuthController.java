package com.example.spring_vfdwebsite.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.spring_vfdwebsite.dtos.authDTOs.LoginRequestDto;
import com.example.spring_vfdwebsite.dtos.authDTOs.LoginResponseDto;
import com.example.spring_vfdwebsite.dtos.authDTOs.LogoutRequestDto;
import com.example.spring_vfdwebsite.dtos.authDTOs.RegisterInitRequestDto;
import com.example.spring_vfdwebsite.dtos.authDTOs.RegisterInitResponseDto;
import com.example.spring_vfdwebsite.dtos.authDTOs.RegisterResponseDto;
import com.example.spring_vfdwebsite.dtos.otpDTOs.ConfirmRegistrationDto;
import com.example.spring_vfdwebsite.dtos.otpDTOs.ResendOtpRequestDto;
import com.example.spring_vfdwebsite.dtos.otpDTOs.ResendOtpResponseDto;
import com.example.spring_vfdwebsite.services.auth.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user authentication")
public class AuthController {

        private final AuthService authService;

        @Operation(summary = "Login user", description = "Authenticate user by email and password, then return JWT token.", responses = {
                        @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDto.class))),
                        @ApiResponse(responseCode = "401", description = "Invalid email or password", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @PostMapping("/login")
        public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
                LoginResponseDto response = authService.loginUser(request);
                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        // Refresh token
        @Operation(summary = "Refresh JWT token", description = "Generate a new JWT access token using a valid refresh token.", responses = {
                        @ApiResponse(responseCode = "200", description = "Token refreshed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDto.class))),
                        @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @PostMapping("/refresh-token")
        public ResponseEntity<LoginResponseDto> refreshToken(@RequestBody Map<String, String> request) {
                String refreshToken = request.get("refreshToken");
                LoginResponseDto response = authService.refreshToken(refreshToken);
                return ResponseEntity.ok(response);
        }

        // Logout
        @Operation(summary = "Logout user", description = "Invalidate the provided refresh token to logout the user.", responses = {
                        @ApiResponse(responseCode = "200", description = "Logout successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.example.spring_vfdwebsite.dtos.ApiResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Refresh token is missing or invalid"),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @PostMapping("/logout")
        public ResponseEntity<com.example.spring_vfdwebsite.dtos.ApiResponse<Void>> logout(
                        @Valid @RequestBody LogoutRequestDto requestDto) {

                authService.logoutUser(requestDto.getRefreshToken());

                return ResponseEntity.ok(com.example.spring_vfdwebsite.dtos.ApiResponse.<Void>builder()
                                .success(true)
                                .message("Logout successful")
                                .build());
        }

        // Bước 1: Khởi tạo đăng ký và gửi OTP
        @Operation(summary = "Initiate user registration", description = "Start the registration process by providing email and password. An OTP will be sent to the email for verification.", responses = {
                        @ApiResponse(responseCode = "200", description = "OTP sent successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterInitResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @PostMapping("/register/init")
        public ResponseEntity<com.example.spring_vfdwebsite.dtos.ApiResponse<RegisterInitResponseDto>> initRegistration(
                        @Valid @RequestBody RegisterInitRequestDto request) {

                RegisterInitResponseDto response = authService.registerInit(request);

                return ResponseEntity
                                .ok(com.example.spring_vfdwebsite.dtos.ApiResponse.<RegisterInitResponseDto>builder()
                                                .success(true)
                                                .message("OTP sent successfully")
                                                .data(response)
                                                .build());
        }

        // Bước 2: Xác thực OTP và hoàn tất đăng ký
        @Operation(summary = "Confirm user registration", description = "Verify OTP and complete the user registration process.", responses = {
                        @ApiResponse(responseCode = "200", description = "Registration completed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid OTP or input data", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @PostMapping("/register/confirm")
        public ResponseEntity<com.example.spring_vfdwebsite.dtos.ApiResponse<RegisterResponseDto>> confirmRegistration(
                        @Valid @RequestBody ConfirmRegistrationDto request) {

                RegisterResponseDto response = authService.registerConfirm(request);

                return ResponseEntity.ok(com.example.spring_vfdwebsite.dtos.ApiResponse.<RegisterResponseDto>builder()
                                .success(true)
                                .message("Registration completed successfully")
                                .data(response)
                                .build());
        }

        // Gửi lại OTP
        @Operation(summary = "Resend OTP", description = "Send a new OTP to the user's email.", responses = {
                        @ApiResponse(responseCode = "200", description = "New OTP sent successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResendOtpResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @PostMapping("/otp/resend")
        public ResponseEntity<com.example.spring_vfdwebsite.dtos.ApiResponse<ResendOtpResponseDto>> resendOtp(
                        @Valid @RequestBody ResendOtpRequestDto request) {

                ResendOtpResponseDto response = authService.resendOtp(request);

                return ResponseEntity.ok(com.example.spring_vfdwebsite.dtos.ApiResponse.<ResendOtpResponseDto>builder()
                                .success(true)
                                .message("New OTP sent successfully")
                                .data(response)
                                .build());
        }
}
