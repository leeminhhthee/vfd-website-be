package com.example.spring_vfdwebsite.filter;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.spring_vfdwebsite.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if (path.startsWith("/api/auth") || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String email = null;

        // Lấy JWT từ header Authorization
        if (authHeader != null && authHeader.trim().startsWith("Bearer ")) {
            jwt = authHeader.trim().substring(7);
            email = jwtService.extractEmail(jwt);
        }

        // Nếu email tồn tại và chưa xác thực trong SecurityContext
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load userDetails từ database
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Kiểm tra token hợp lệ
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // Lấy thông tin isAdmin từ claim
                boolean isAdmin = jwtService.extractIsAdmin(jwt);

                // Tạo authority dựa trên isAdmin
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                        isAdmin ? "ROLE_ADMIN" : "ROLE_USER");

                // Tạo authentication token
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Không cần credentials
                        Collections.singletonList(authority));
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Đặt authentication vào SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Tiếp tục filter chain
        filterChain.doFilter(request, response);
    }
}
