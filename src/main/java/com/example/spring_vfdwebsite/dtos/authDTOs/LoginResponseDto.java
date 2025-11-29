package com.example.spring_vfdwebsite.dtos.authDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Login response payload")
public class LoginResponseDto {

    // @Schema(description = "Unique identifier of the user", example = "1")
    // private Integer id;

    // @Schema(description = "Full name of the user", example = "Vo Van C")
    // private String fullName;

    // @Schema(description = "Email address of the user", example = "vovanc@example.com")
    // private String email;

    // @Schema(description = "Profile image URL of the user", example = "http://example.com/image.jpg")
    // private String imageUrl;

    // @Schema(description = "Indicates if the user is an admin", example = "true")
    // private boolean isAdmin;

    @Schema(description = "User information")
    private UserDto user;

    @Schema(description = "JWT Access Token")
    private String accessToken;

     @Schema(description = "JWT Refresh Token")
    private String refreshToken;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserDto {

        @Schema(description = "Unique identifier of the user", example = "1")
        private Integer id;

        @Schema(description = "Full name of the user", example = "Vo Van C")
        private String fullName;

        @Schema(description = "Email address of the user", example = "vovanc@example.com")
        private String email;

        @Schema(description = "Profile image URL of the user", example = "http://example.com/image.jpg")
        private String imageUrl;

        @Schema(description = "Indicates if the user is an admin", example = "true")
        private boolean isAdmin;
    }
}
