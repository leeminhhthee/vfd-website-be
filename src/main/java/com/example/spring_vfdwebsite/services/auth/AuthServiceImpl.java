package com.example.spring_vfdwebsite.services.auth;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_vfdwebsite.dtos.activityLogDTOs.ActivityLogCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.authDTOs.LoginRequestDto;
import com.example.spring_vfdwebsite.dtos.authDTOs.LoginResponseDto;
import com.example.spring_vfdwebsite.dtos.authDTOs.RegisterInitRequestDto;
import com.example.spring_vfdwebsite.dtos.authDTOs.RegisterInitResponseDto;
import com.example.spring_vfdwebsite.dtos.authDTOs.RegisterResponseDto;
import com.example.spring_vfdwebsite.dtos.otpDTOs.ConfirmRegistrationDto;
import com.example.spring_vfdwebsite.dtos.otpDTOs.ResendOtpRequestDto;
import com.example.spring_vfdwebsite.dtos.otpDTOs.ResendOtpResponseDto;
import com.example.spring_vfdwebsite.entities.PendingUser;
import com.example.spring_vfdwebsite.entities.RefreshToken;
import com.example.spring_vfdwebsite.entities.User;
import com.example.spring_vfdwebsite.exceptions.HttpException;
import com.example.spring_vfdwebsite.repositories.PendingUserJpaRepository;
import com.example.spring_vfdwebsite.repositories.RefreshTokenRepository;
import com.example.spring_vfdwebsite.repositories.UserJpaRepository;
import com.example.spring_vfdwebsite.services.JwtService;
import com.example.spring_vfdwebsite.services.activityLog.ActivityLogService;
import com.example.spring_vfdwebsite.services.mailOtp.OtpService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserJpaRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PendingUserJpaRepository pendingUserRepository;
    private final OtpService otpService;
    private final ActivityLogService activityLogService;

    @Override
    public LoginResponseDto loginUser(LoginRequestDto requestDto) {
        // Basic input validation
        if (requestDto.getEmail() == null || requestDto.getEmail().isBlank()) {
            throw new HttpException("Email is required", HttpStatus.BAD_REQUEST);
        }

        if (requestDto.getPassword() == null || requestDto.getPassword().isBlank()) {
            throw new HttpException("Password is required", HttpStatus.BAD_REQUEST);
        }

        // Authenticate using AuthenticationManager (will consult UserDetailsService)
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword()));
        } catch (AuthenticationException ex) {
            throw new HttpException("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }

        // Load user entity
        User user = userRepository.findByEmail(requestDto.getEmail()).stream().findFirst()
                .orElseThrow(() -> new HttpException("Invalid email or password", HttpStatus.UNAUTHORIZED));

        // Generate JWT
        String accessToken = jwtService.generateAccessToken(user);

        // Generate refresh token
        String refreshTokenStr = jwtService.generateRefreshToken(user);

        // Lưu refresh token vào DB
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenStr)
                .user(user)
                .expiryDate(new Timestamp(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000)) // 7 ngày
                .build();
        refreshTokenRepository.save(refreshToken);

        // ===== GHI LOG LOGIN =====
        ActivityLogCreateRequestDto logDto = ActivityLogCreateRequestDto.builder()
                .actionType("LOGIN")
                .targetTable("users")
                .targetId(user.getId())
                .description("User login")
                .build();
        activityLogService.createActivityLogResponseDto(user, logDto);

        // Build response
        return LoginResponseDto.builder()
                .user(LoginResponseDto.UserDto.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .imageUrl(user.getImageUrl())
                        .isAdmin(user.isAdmin())
                        .build())
                .accessToken(accessToken)
                .refreshToken(refreshTokenStr)
                .build();
    }

    @Override
    public LoginResponseDto refreshToken(String refreshTokenStr) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenStr)
                .orElseThrow(() -> new HttpException("Invalid refresh token", HttpStatus.UNAUTHORIZED));

        if (refreshToken.getExpiryDate().before(new Timestamp(System.currentTimeMillis()))) {
            throw new HttpException("Refresh token expired", HttpStatus.UNAUTHORIZED);
        }

        User user = refreshToken.getUser();

        // Sinh access token mới
        String newAccessToken = jwtService.generateAccessToken(user);

        // Optionally: tạo refresh token mới và xóa cũ
        refreshTokenRepository.delete(refreshToken);

        String newRefreshTokenStr = jwtService.generateRefreshToken(user);
        RefreshToken newRefreshToken = RefreshToken.builder()
                .token(newRefreshTokenStr)
                .user(user)
                .expiryDate(new Timestamp(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000)) // 7 ngày
                .build();
        refreshTokenRepository.save(newRefreshToken);

        // ===== GHI LOG REFRESH TOKEN =====
        ActivityLogCreateRequestDto logDto = ActivityLogCreateRequestDto.builder()
                .actionType("REFRESH_TOKEN")
                .targetTable("users")
                .targetId(user.getId())
                .description("User refreshed access token")
                .build();
        activityLogService.createActivityLogResponseDto(user, logDto);

        return LoginResponseDto.builder()
                .user(LoginResponseDto.UserDto.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .imageUrl(user.getImageUrl())
                        .isAdmin(user.isAdmin())
                        .build())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshTokenStr)
                .build();
    }

    @Override
    public RegisterInitResponseDto registerInit(RegisterInitRequestDto requestDto) {

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new HttpException("Email is already registered", HttpStatus.CONFLICT);
        }

        // Xóa pending user cũ nếu có
        pendingUserRepository.deleteByEmail(requestDto.getEmail());

        // Tạo pending user mới
        PendingUser pendingUser = PendingUser.builder()
                .email(requestDto.getEmail())
                .fullName(requestDto.getFullName())
                .phoneNumber(requestDto.getPhoneNumber())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .isAdmin(false)
                .build();
        pendingUserRepository.save(pendingUser);

        // ===== GHI LOG REGISTER INIT =====
        ActivityLogCreateRequestDto logDto = ActivityLogCreateRequestDto.builder()
                .actionType("REGISTER_INIT")
                .targetTable("users")
                .targetId(null) // Chưa có user ID vì chưa hoàn tất đăng ký
                .description("User initiated registration")
                .build();
        activityLogService.createActivityLogResponseDto(null, logDto);

        // Gửi OTP đến email
        otpService.sendOtp(requestDto.getEmail(), requestDto.getFullName());

        return RegisterInitResponseDto.builder()
                .email(requestDto.getEmail())
                .message("OTP has been sent to your email. Please verify your email with the OTP sent.")
                .build();
    }

    // Xác thực OTP và hoàn tất đăng ký
    @Override
    public RegisterResponseDto registerConfirm(ConfirmRegistrationDto requestDto) {
        // Kiểm tra pending user
        PendingUser pendingUser = pendingUserRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new HttpException("No registration initiated for this email", HttpStatus.NOT_FOUND));

        // Xác thực OTP
        if (!otpService.verifyOtp(requestDto.getEmail(), requestDto.getOtp())) {
            throw new HttpException("Invalid or expired OTP", HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra pending user chưa hết hạn
        if (pendingUser.getExpiresAt().isBefore(LocalDateTime.now())) {
            pendingUserRepository.deleteByEmail(requestDto.getEmail());
            throw new HttpException("Registration session expired. Please register again.", HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra email đã tồn tại
        if (userRepository.existsByEmail(pendingUser.getEmail())) {
            pendingUserRepository.deleteByEmail(requestDto.getEmail());
            throw new HttpException("Email already exists", HttpStatus.CONFLICT);
        }

        // Tạo user mới
        User user = User.builder()
                .email(pendingUser.getEmail())
                .fullName(pendingUser.getFullName())
                .phoneNumber(pendingUser.getPhoneNumber())
                .password(pendingUser.getPassword())
                .isAdmin(false) // Mặc định không phải admin
                .build();
        userRepository.save(user);

        // ===== GHI LOG REGISTER COMPLETE =====
        ActivityLogCreateRequestDto logDto = ActivityLogCreateRequestDto.builder()
                .actionType("REGISTER_COMPLETE")
                .targetTable("users")
                .targetId(user.getId())
                .description("User completed registration")
                .build();
        activityLogService.createActivityLogResponseDto(user, logDto);

        // Xóa pending user
        pendingUserRepository.delete(pendingUser);

        String token = jwtService.generateAccessToken(user);

        return RegisterResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .message("Registration completed successfully.")
                .accessToken(token)
                .build();
    }

    @Override
    @Transactional
    public ResendOtpResponseDto resendOtp(ResendOtpRequestDto requestDto) {
        // Kiểm tra pending user
        PendingUser pendingUser = pendingUserRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new HttpException("No registration initiated for this email", HttpStatus.NOT_FOUND));

        // Kiểm tra pending user chưa hết hạn
        if (pendingUser.getExpiresAt().isBefore(LocalDateTime.now())) {
            pendingUserRepository.deleteByEmail(requestDto.getEmail());
            throw new HttpException("Registration session expired. Please register again.", HttpStatus.BAD_REQUEST);
        }

        // Gửi lại OTP đến email
        otpService.sendOtp(requestDto.getEmail(), pendingUser.getFullName());

        // ===== GHI LOG RESEND OTP =====
        ActivityLogCreateRequestDto logDto = ActivityLogCreateRequestDto.builder()
                .actionType("RESEND_OTP")
                .targetTable("users")
                .targetId(null) // Chưa có user ID vì chưa hoàn tất đăng ký
                .description("User requested to resend OTP")
                .build();
        activityLogService.createActivityLogResponseDto(null, logDto);

        return ResendOtpResponseDto.builder()
                .email(requestDto.getEmail())
                .message("OTP has been resent to your email.")
                .cooldownSeconds(otpService.getRemainingCooldownSeconds(requestDto.getEmail()))
                .build();
    }

    @Override
    @Transactional
    public void logoutUser(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken).ifPresent(token -> {
            User user = token.getUser(); // Lấy user liên quan đến refresh token

            // Xóa refresh token khỏi DB
            refreshTokenRepository.delete(token);

            // ===== GHI LOG LOGOUT =====
            ActivityLogCreateRequestDto logDto = ActivityLogCreateRequestDto.builder()
                    .actionType("LOGOUT")
                    .targetTable("users")
                    .targetId(user.getId())
                    .description("User logged out")
                    .build();
            activityLogService.createActivityLogResponseDto(user, logDto);
            // =========================
        });
    }

}
