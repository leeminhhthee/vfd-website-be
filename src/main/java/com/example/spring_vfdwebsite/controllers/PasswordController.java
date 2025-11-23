package com.example.spring_vfdwebsite.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.spring_vfdwebsite.dtos.otpDTOs.PasswordDtos;
import com.example.spring_vfdwebsite.services.mailOtp.PasswordService;

@RestController
@RequestMapping("/api/auth/password")
@RequiredArgsConstructor
@Tag(name = "Password", description = "APIs for password management")
public class PasswordController {

    private final PasswordService passwordService;

    // Change password - request OTP
    @Operation(summary = "Request OTP for changing password", description = "Send an OTP to the user's email to authorize password change.", responses = {
            @ApiResponse(responseCode = "200", description = "OTP sent successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/change/request-otp")
    public ResponseEntity<?> requestChangePasswordOtp(@RequestBody @Valid PasswordDtos.ChangePasswordOtpRequest req) {
        passwordService.sendChangePasswordOtp(req.getEmail());
        return ResponseEntity.ok().body("{\"message\":\"OTP sent\"}");
    }

    // Change password - verify OTP and change password
    @Operation(summary = "Change password using OTP", description = "Change the user's password by providing the OTP sent to their email along with the old and new passwords.", responses = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/change")
    public ResponseEntity<?> changePassword(@RequestBody @Valid PasswordDtos.ChangePasswordRequest req) {
        passwordService.changePasswordWithOtp(
                req.getEmail(),
                req.getOtp(),
                req.getOldPassword(),
                req.getNewPassword()
        );
        return ResponseEntity.ok().body("{\"message\":\"Password changed\"}");
    }

    // Get change password OTP cooldown
    @Operation(summary = "Get cooldown time for change password OTP", description = "Retrieve the remaining cooldown time in seconds before a new OTP can be requested for changing password.", responses = {
            @ApiResponse(responseCode = "200", description = "Cooldown time retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"cooldown_remaining_seconds\":30}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/change/cooldown")
    public ResponseEntity<?> getChangeCooldown(@RequestParam String email) {
        long seconds = passwordService.getChangePasswordCooldown(email);
        return ResponseEntity.ok().body("{\"cooldown_remaining_seconds\":" + seconds + "}");
    }

    // --- FORGOT ---
    @Operation(summary = "Request OTP for forgot password", description = "Send an OTP to the user's email to authorize password reset.", responses = {
            @ApiResponse(responseCode = "200", description = "OTP sent successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/forgot/request-otp")
    public ResponseEntity<?> requestForgotPasswordOtp(@RequestBody @Valid PasswordDtos.ForgotPasswordOtpRequest req) {
        passwordService.sendForgotPasswordOtp(req.getEmail());
        return ResponseEntity.ok().body("{\"message\":\"OTP sent\"}");
    }

    @Operation(summary = "Verify OTP for forgot password", description = "Verify the OTP sent to the user's email and issue a reset token for password reset.", responses = {
            @ApiResponse(responseCode = "200", description = "OTP verified successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PasswordDtos.VerifyForgotOtpResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/forgot/verify-otp")
    public ResponseEntity<?> verifyForgotOtp(@RequestBody @Valid PasswordDtos.VerifyForgotOtpRequest req) {
        String resetToken = passwordService.verifyForgotPasswordOtpIssueToken(req.getEmail(), req.getOtp());
        return ResponseEntity.ok(new PasswordDtos.VerifyForgotOtpResponse(resetToken));
    }

    @Operation(summary = "Reset password using reset token", description = "Reset the user's password by providing the reset token along with the new password.", responses = {
            @ApiResponse(responseCode = "200", description = "Password reset successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/forgot/reset")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid PasswordDtos.ResetPasswordRequest req) {
        passwordService.resetPasswordWithToken(req.getResetToken(), req.getNewPassword(), req.getConfirmPassword());
        return ResponseEntity.ok().body("{\"message\":\"Password reset\"}");
    }

    @Operation(summary = "Get cooldown time for forgot password OTP", description = "Retrieve the remaining cooldown time in seconds before a new OTP can be requested for forgot password.", responses = {
            @ApiResponse(responseCode = "200", description = "Cooldown time retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"cooldown_remaining_seconds\":30}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/forgot/cooldown")
    public ResponseEntity<?> getForgotCooldown(@RequestParam String email) {
        long seconds = passwordService.getForgotPasswordCooldown(email);
        return ResponseEntity.ok().body("{\"cooldown_remaining_seconds\":" + seconds + "}");
    }
    
}
