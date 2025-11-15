package com.example.spring_vfdwebsite.dtos.userDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.*;

@Schema(description = "DTO for updating an existing user")
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequestDto {

        @Schema(description = "Unique identifier of the user", example = "1")
        private Integer id;

        @Schema(description = "Full name of the user", example = "Tran Van B")
        private String fullName;

        @Schema(description = "Email address of the user", example = "tranvanb@example.com")
        @Email(message = "Invalid email format")
        private String email;

        @Schema(description = "Phone number of the user", example = "0912345678")
        private String phoneNumber;

        @Schema(description = "URL to the user's profile image", example = "https://example.com/images/user1.png")
        private String imageUrl;

        @Schema(description = "User's hobbies or interests", example = "Reading, Volleyball, Traveling")
        private String hobby;

        @Schema(description = "Password of the user (optional, only if being updated)", example = "newpassword123")
        private String password;

        @Schema(description = "Flag indicating whether the user is an admin", example = "false")
        private Boolean isAdmin;

        // Explicit getter to provide a stable accessor name and allow null when not
        // updating
        public Boolean getIsAdmin() {
                return this.isAdmin;
        }
}
