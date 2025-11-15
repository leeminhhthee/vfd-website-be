package com.example.spring_vfdwebsite.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.spring_vfdwebsite.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey; // Base64 encoded key, đưa vào application.properties

    @Value("${jwt.expiration}")
    private long jwtExpiration; // expiration thời gian ms, ví dụ 86400000 = 24h

    // --- Tạo SecretKey từ Base64 ---
    private SecretKey getSigningKey() {
        byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // --- Tạo token ---
    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    // --- Tạo access token cho User ---
    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("isAdmin", user.isAdmin()); // claim quyền
        claims.put("type", "access_token"); // token type identifier

        return createToken(claims, user.getEmail(), jwtExpiration);
    }

    // --- Extract tất cả claim ---
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // --- Extract claim cụ thể ---
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // --- Extract email ---
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // --- Extract isAdmin ---
    public boolean extractIsAdmin(String token) {
        return extractClaim(token, claims -> claims.get("isAdmin", Boolean.class));
    }

    // --- Extract token type ---
    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get("type", String.class));
    }

    // --- Extract expiration ---
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // --- Check token expired ---
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // --- Validate token ---
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        final String tokenType = extractTokenType(token);
        return (email.equals(userDetails.getUsername()))
                && !isTokenExpired(token)
                && "access_token".equals(tokenType); // chỉ access token mới hợp lệ
    }

    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("type", "refresh_token");
        long jwtExpiration = 7L * 24 * 60 * 60 * 1000; // 7 ngày
        return createToken(claims, user.getEmail(), jwtExpiration);
    }
}
