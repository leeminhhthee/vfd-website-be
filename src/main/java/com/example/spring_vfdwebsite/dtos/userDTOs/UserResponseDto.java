package com.example.spring_vfdwebsite.dtos.userDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "DTO for User response")
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

        @Schema(description = "Unique identifier of the user", example = "1")
        private Integer id;

        @Schema(description = "Full name of the user", example = "Tran Van B")
        private String fullName;

        @Schema(description = "Email address of the user", example = "tranvanb@example.com")
        private String email;

        @Schema(description = "Phone number of the user", example = "0912345678")
        private String phoneNumber;

        @Schema(description = "URL to the user's profile image", example = "https://example.com/images/user1.png")
        private String imageUrl;

        @Schema(description = "User's hobbies or interests", example = "Reading, Volleyball, Traveling")
        private String hobby;

        @Schema(description = "Flag indicating whether the user is an admin", example = "false")
        private Boolean isAdmin;

        @Schema(description = "Timestamp when the record was created", example = "2025-01-15T10:30:00")
        private LocalDateTime createdAt;

        @Schema(description = "Timestamp when the record was last updated", example = "2025-03-05T15:20:00")
        private LocalDateTime updatedAt;

        @Schema(description = "Flag indicating whether the user is active", example = "true")
        private Boolean isActive;
}
