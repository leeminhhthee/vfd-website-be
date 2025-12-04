package com.example.spring_vfdwebsite.services.mailOtp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_vfdwebsite.annotations.LoggableAction;
import com.example.spring_vfdwebsite.dtos.activityLogDTOs.ActivityLogCreateRequestDto;
import com.example.spring_vfdwebsite.entities.User;
import com.example.spring_vfdwebsite.exceptions.HttpException;
import com.example.spring_vfdwebsite.repositories.UserJpaRepository;
import com.example.spring_vfdwebsite.services.activityLog.ActivityLogService;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordService {

    private final UserJpaRepository userRepository;
    private final OtpRedisService otpRedisService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String RESET_TOKEN_PREFIX = "reset_token:";
    private static final long RESET_TOKEN_TTL_MINUTES = 10; // token đặt lại mật khẩu còn hiệu lực 10 phút

    private final SecureRandom secureRandom = new SecureRandom();

    private final ActivityLogService activityLogService;

    /**
     * Gửi OTP để thay đổi mật khẩu (user đã đăng nhập)
     */
    @LoggableAction(value = "SEND_CHANGE_PASSWORD_OTP", entity = "users", description = "Send change password OTP")
    public void sendChangePasswordOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new HttpException("User not found", HttpStatus.NOT_FOUND));

        otpRedisService.sendChangePasswordOtp(email, user.getFullName());
        log.info("Change password OTP requested for user: {}", email);

        ActivityLogCreateRequestDto logDto = ActivityLogCreateRequestDto.builder()
                .actionType("SEND_CHANGE_PASSWORD_OTP")
                .targetTable("users")
                .targetId(user.getId())
                .description("Send change password OTP")
                .build();
        activityLogService.createActivityLogResponseDto(user, logDto);
    }

    /**
     * Thay đổi mật khẩu sau khi xác thực OTP (user đã đăng nhập)
     */
    @Transactional
    public void changePasswordWithOtp(String email, String otp, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new HttpException("User not found", HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new HttpException("Current password is incorrect", HttpStatus.BAD_REQUEST);
        }

        if (!otpRedisService.verifyChangePasswordOtp(email, otp)) {
            throw new HttpException("Invalid or expired OTP", HttpStatus.BAD_REQUEST);
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new HttpException("New password must be different from current password", HttpStatus.BAD_REQUEST);
        }

        validatePasswordPolicy(newPassword);

        user.setPassword(passwordEncoder.encode(newPassword));

        ActivityLogCreateRequestDto logDto = ActivityLogCreateRequestDto.builder()
                .actionType("CHANGE_PASSWORD_WITH_OTP")
                .targetTable("users")
                .targetId(user.getId())
                .description("User changed password with OTP")
                .build();
        activityLogService.createActivityLogResponseDto(user, logDto);

        userRepository.save(user);

        log.info("Password changed successfully for user: {}", email);
    }

    /**
     * Gửi OTP cho quên mật khẩu
     */
    @LoggableAction(value = "SEND_FORGOT_PASSWORD_OTP", entity = "users", description = "Send forgot password OTP")
    public void sendForgotPasswordOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new HttpException("User not found", HttpStatus.NOT_FOUND));

        otpRedisService.sendForgotPasswordOtp(email, user.getFullName());
        log.info("Forgot password OTP sent for user: {}", email);

        // Ghi log DB
        ActivityLogCreateRequestDto logDto = ActivityLogCreateRequestDto.builder()
                .actionType("SEND_FORGOT_PASSWORD_OTP")
                .targetTable("users")
                .targetId(user.getId())
                .description("Send forgot password OTP")
                .build();
        activityLogService.createActivityLogResponseDto(user, logDto);
    }

    /**
     * Xác thực OTP quên mật khẩu -> cấp resetToken tạm (TTL 10 phút)
     */
    @LoggableAction(value = "PASSWORD_RESET_INIT", entity = "users", description = "Verify forgot password OTP and issue reset token")
    public String verifyForgotPasswordOtpIssueToken(String email, String otp) {
        // Tìm user (để tránh lộ thông tin, vẫn thống nhất 404 nếu không tồn tại)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new HttpException("User not found", HttpStatus.NOT_FOUND));

        if (!otpRedisService.verifyForgotPasswordOtp(email, otp)) {
            throw new HttpException("Invalid or expired OTP", HttpStatus.BAD_REQUEST);
        }

        // Sinh reset token ngẫu nhiên, base64-url-safe
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        String resetToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        // Lưu resetToken -> email vào Redis
        String key = RESET_TOKEN_PREFIX + resetToken;
        redisTemplate.opsForValue().set(key, email, RESET_TOKEN_TTL_MINUTES, TimeUnit.MINUTES);

        log.info("Issued reset token for email {} with TTL {} minutes", email, RESET_TOKEN_TTL_MINUTES);
        
        // Ghi log DB
        ActivityLogCreateRequestDto logDto = ActivityLogCreateRequestDto.builder()
                .actionType("PASSWORD_RESET_INIT")
                .targetTable("users")
                .targetId(user.getId())
                .description("Verify forgot password OTP and issue reset token")
                .build();
        activityLogService.createActivityLogResponseDto(user, logDto);

        return resetToken;
    }

    /**
     * Reset mật khẩu với resetToken tạm
     */
    @Transactional
    @LoggableAction(value = "PASSWORD_RESET_COMPLETE", entity = "users", description = "Reset password with reset token")
    public void resetPasswordWithToken(String resetToken, String newPassword, String confirmPassword) {
        if (resetToken == null || resetToken.isBlank()) {
            throw new HttpException("Reset token is required", HttpStatus.BAD_REQUEST);
        }

        String key = RESET_TOKEN_PREFIX + resetToken;
        String email = redisTemplate.opsForValue().get(key);
        if (email == null) {
            throw new HttpException("Invalid or expired reset token", HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new HttpException("User not found", HttpStatus.NOT_FOUND));

        if (!newPassword.equals(confirmPassword)) {
            throw new HttpException("Password confirmation does not match", HttpStatus.BAD_REQUEST);
        }

        validatePasswordPolicy(newPassword);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Xóa token sau khi dùng
        redisTemplate.delete(key);

        log.info("Password reset successfully for user: {}", email);

        // Ghi log DB
        ActivityLogCreateRequestDto logDto = ActivityLogCreateRequestDto.builder()
                .actionType("PASSWORD_RESET_COMPLETE")
                .targetTable("users")
                .targetId(user.getId())
                .description("Reset password with reset token")
                .build();
        activityLogService.createActivityLogResponseDto(user, logDto);
    }

    /**
     * Lấy thời gian cooldown còn lại cho việc gửi OTP thay đổi mật khẩu
     */
    public long getChangePasswordCooldown(String email) {
        return otpRedisService.getRemainingCooldown(email, "CHANGE_PASSWORD");
    }

    /**
     * Lấy thời gian cooldown còn lại cho việc gửi OTP quên mật khẩu
     */
    public long getForgotPasswordCooldown(String email) {
        return otpRedisService.getRemainingCooldown(email, "FORGOT_PASSWORD");
    }

    private void validatePasswordPolicy(String newPassword) {
        // Ví dụ chính sách tối thiểu – bạn có thể thay đổi theo nhu cầu
        if (newPassword.length() < 8) {
            throw new HttpException("Password must be at least 8 characters", HttpStatus.BAD_REQUEST);
        }
        if (!newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).+$")) {
            throw new HttpException("Password must contain uppercase, lowercase, number, and special character",
                    HttpStatus.BAD_REQUEST);
        }
    }
}
