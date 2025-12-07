package com.example.spring_vfdwebsite.dtos.userDTOs;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Schema(description = "DTO for creating a new user")
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateRequestDto {

        @Schema(description = "Full name of the user", example = "Tran Van B")
        @NotBlank(message = "Full name is required")
        @Size(max = 50, message = "Full name must be less than 50 characters")
        private String fullName;

        @Schema(description = "Email address of the user", example = "tranvanb@example.com")
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        private String email;

        @Schema(description = "Phone number of the user", example = "0912345678")
        private String phoneNumber;

        @Schema(description = "URL to the user's profile image", example = "https://example.com/images/user1.png")
        private String imageUrl;

        @Schema(description = "Birthday of the user", example = "1992-08-15")
        private LocalDate birthday;

        @Schema(description = "Gender of the user", example = "Male")
        private String gender;

        @Schema(description = "Address of the user", example = "456 Another St, City, Country")
        private String address;

        @Schema(description = "Level of the user", example = "university")
        private String level;

        @Schema(description = "Education background of the user", example = "Bachelor's Degree in Information Technology")
        private String education;

        @Schema(description = "Accumulated points of the user", example = "1500")
        private Integer accumulatedPoints;

        @Schema(description = "Password of the user", example = "123456")
        private String password;

        @Builder.Default
        @Schema(description = "Flag indicating whether the user is an admin", example = "false")
        private Boolean isAdmin = false;

        @Builder.Default
        @Schema(description = "Flag indicating whether the user is active", example = "true")
        private Boolean isActive = true;

        // Explicit getter to avoid 'is' prefix confusion in
        // serialization/deserialization
        public Boolean getIsAdmin() {
                return this.isAdmin;
        }
}
