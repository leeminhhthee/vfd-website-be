package com.example.spring_vfdwebsite.dtos.authDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
// import jakarta.validation.constraints.AssertTrue;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Login request payload")

public class LoginRequestDto {
    @Schema(description = "User email", example = "vovanc@example.com")
    private String email;

    @Schema(description = "User password", example = "12345678")
    private String password;

    // @AssertTrue(message = "Email is required")
    // public boolean isEmailProvided() {
    //     return (email != null && !email.isBlank());
    // }
}
    