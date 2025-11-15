package com.example.spring_vfdwebsite.services.auth;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_vfdwebsite.dtos.authDTOs.LoginRequestDto;
import com.example.spring_vfdwebsite.dtos.authDTOs.LoginResponseDto;
import com.example.spring_vfdwebsite.entities.RefreshToken;
import com.example.spring_vfdwebsite.entities.User;
import com.example.spring_vfdwebsite.exceptions.HttpException;
import com.example.spring_vfdwebsite.repositories.RefreshTokenRepository;
import com.example.spring_vfdwebsite.repositories.UserJpaRepository;
import com.example.spring_vfdwebsite.services.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserJpaRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

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

        // Build response
        return LoginResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .imageUrl(user.getImageUrl())
                .isAdmin(user.isAdmin())
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

        return LoginResponseDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .isAdmin(user.isAdmin())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshTokenStr)
                .build();
    }
}
