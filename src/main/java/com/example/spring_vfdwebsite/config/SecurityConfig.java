package com.example.spring_vfdwebsite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.spring_vfdwebsite.exceptions.CustomAccessDeniedHandler;
import com.example.spring_vfdwebsite.exceptions.CustomAuthenticationEntryPoint;
import com.example.spring_vfdwebsite.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final CustomAccessDeniedHandler customAccessDeniedHandler;
        private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .exceptionHandling(exceptionHandlingCustomizer -> exceptionHandlingCustomizer
                                                .authenticationEntryPoint(this.customAuthenticationEntryPoint)
                                                .accessDeniedHandler(this.customAccessDeniedHandler))
                                .authorizeHttpRequests(auth -> auth
                                                // ✅ Cho phép truy cập Swagger mà không cần đăng nhập
                                                .requestMatchers(
                                                                "/v3/api-docs/**",
                                                                "/swagger-ui/**",
                                                                "/swagger-ui.html"
                                                                // ,
                                                                // "/api/board-directors/**",
                                                                // "/api/users/**"
                                                                )
                                                .permitAll()

                                                // ✅ Các endpoint public khác (nếu có)
                                                .requestMatchers("/api/auth/**").permitAll()
                                                // .requestMatchers("/api/users/**").hasRole("ADMIN")

                                                // 🔒 Các endpoint còn lại yêu cầu xác thực
                                                .anyRequest().authenticated())
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }
}